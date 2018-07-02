package com.tokopedia.product.edit.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import java.util.ArrayList
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.networklib.util.RestClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.activity.ProductAddVideoRecommendationActivity
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.edit.listener.ProductAddVideoView
import com.tokopedia.product.edit.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.listener.VideoChosenListener
import com.tokopedia.product.edit.mapper.VideoMapper
import com.tokopedia.product.edit.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.presenter.ProductAddVideoPresenter
import com.tokopedia.product.edit.viewmodel.*

class ProductAddVideoFragment : BaseListFragment<ProductAddVideoBaseViewModel, ProductAddVideoAdapterTypeFactory>(), ProductAddVideoView, VideoChosenListener, SectionVideoRecommendationListener {

    override val contextView: Context get() = activity

    var videoIDs: ArrayList<String> = ArrayList()
    private var youtubeVideoRecommendationList: ArrayList<YoutubeVideoModel> = ArrayList()
    private lateinit var productAddVideoPresenter : ProductAddVideoPresenter
    private lateinit var listener : Listener
    private val mapper = VideoMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        GraphqlClient.init(activity.applicationContext)
        RestClient.init(activity.applicationContext)

        productAddVideoPresenter = ProductAddVideoPresenter()
        productAddVideoPresenter.attachView(this)

        if(activity.intent != null){
            videoIDs = activity.intent.getStringArrayListExtra(EXTRA_VIDEOS_LINKS)
        }
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_product_add_video, container, false)

        val btnTambah: Button = view.findViewById(R.id.button_tambah)

        btnTambah.setOnClickListener({
            if(isVideoChosenSlotNotFull()) {
                val dialog = ProductAddVideoDialogFragment()
                dialog.show(fragmentManager, ProductAddVideoDialogFragment.TAG)
            }
        })

        return view
    }

    override fun getAdapterTypeFactory(): ProductAddVideoAdapterTypeFactory {
        return ProductAddVideoAdapterTypeFactory(this, this)
    }

    override fun onItemClicked(t: ProductAddVideoBaseViewModel?) {

    }

    override fun getScreenName(): String {
        return getString(R.string.title_activity_video)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        productAddVideoPresenter.getYoutubaDataVideoChosen(videoIDs)
    }

    override fun renderListData(productAddVideoBaseViewModelList: List<ProductAddVideoBaseViewModel>) {
        renderList(productAddVideoBaseViewModelList)
    }

    private fun updateListData(productAddVideoBaseViewModel : ProductAddVideoBaseViewModel, type : Int){
        if(type == ADD_VIDEO_CHOSEN) {
            for (i in 0 until adapter.data.size) {
                val productAddVideoBaseViewModels = adapter.data[i]
                if (productAddVideoBaseViewModels is EmptyVideoViewModel) {
                    adapter.clearElement(productAddVideoBaseViewModels)
                    val titleVideoChosenViewModel = TitleVideoChosenViewModel()
                    adapter.addElement(titleVideoChosenViewModel)
                    break
                }
            }
            adapter.addElement(productAddVideoBaseViewModel)
        } else {
            adapter.clearElement(productAddVideoBaseViewModel)
            if(videoIDs.size == 0){
                for (i in 0 until adapter.data.size) {
                    val productAddVideoBaseViewModels = adapter.data[i]
                    if (productAddVideoBaseViewModels is TitleVideoChosenViewModel) {
                        adapter.clearElement(productAddVideoBaseViewModels)
                        val emptyVideoViewModel = EmptyVideoViewModel()
                        adapter.addElement(emptyVideoViewModel)
                        break
                    }
                }
            }
        }
    }

    private fun isVideoChosenSlotNotFull(): Boolean {
        if(videoIDs.size >= 3) {
            showSnackbarRed(getString(R.string.product_add_message_slot_full_video_choseen))
        }
        return videoIDs.size < 3
    }

    override fun addVideoIDfromURL(videoID: String) {
        for(id in videoIDs){
            if(id == videoID){
                showSnackbarRed(getString(R.string.product_add_message_exist_video_choseen))
                return
            }
        }
        productAddVideoPresenter.getYoutubaDataVideoUrl(videoID)
    }

    override fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        youtubeVideoRecommendationList = youtubeVideoModelArrayList
        val mapper = VideoRecommendationMapper()
        var videoRecommendationViewModelList = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList)
        val videoViewModelList = adapter.data
        for(videoViewModel in videoViewModelList){
            if(videoViewModel is VideoViewModel){
                for(videoRecommendationViewModel in videoRecommendationViewModelList){
                    if(videoRecommendationViewModel.videoID == videoViewModel.videoID){
                        videoRecommendationViewModel.chosen = true
                        videoViewModel.recommendation = true
                    }
                }
            }
        }
        adapter.notifyDataSetChanged()
        listener.onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList)
    }

    override fun onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, ProductAddVideoFragment.MAX_VIDEO)
        productAddVideoPresenter.getVideoRecommendation("iphone", MAX_VIDEO_RECOMMENDATION)
    }

    override fun onSuccessGetYoutubeDataVideoUrl(youtubeVideoModel: YoutubeVideoModel) {
        videoIDs.add(youtubeVideoModel.id)
        updateListData(mapper.transformDataToVideoViewModel(youtubeVideoModel), ADD_VIDEO_CHOSEN)
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, ProductAddVideoFragment.MAX_VIDEO)
        showSnackbarGreen(getString(R.string.product_add_message_success_add_video_choseen))
    }

    override fun onErrorGetVideoData(e: Throwable) {

    }


    override fun onShowMoreClicked() {
        val intent = Intent(activity, ProductAddVideoRecommendationActivity::class.java)
        intent.putParcelableArrayListExtra(EXTRA_VIDEO_RECOMMENDATION, youtubeVideoRecommendationList)
        intent.putStringArrayListExtra(EXTRA_VIDEOS_LINKS, videoIDs)
        startActivityForResult(intent, REQUEST_CODE_GET_VIDEO_RECOMMENDATION)
    }

    override fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
        if (videoIDs.contains(videoRecommendationViewModel.videoID)) {
            showSnackbarRed(getString(R.string.product_add_message_exist_video_choseen))
        } else {
            if(isVideoChosenSlotNotFull()) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.product_add_title_dialog_add_video)
                builder.setMessage(R.string.product_add_description_dialog_add_video)
                builder.setCancelable(true)
                builder.setPositiveButton(R.string.label_add, { dialog, id ->
                    videoIDs.add(videoRecommendationViewModel.videoID!!)
                    updateListData(mapper.transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel), ADD_VIDEO_CHOSEN)
                    (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
                    listener.onVideoChosenAdded(videoRecommendationViewModel)
                    showSnackbarGreen(getString(R.string.product_add_message_success_add_video_recommendation))
                })
                builder.setNegativeButton(R.string.label_cancel, { dialog, id -> dialog.cancel() })

                val alert = builder.create()
                alert.show()
            }
        }

    }

    override fun onVideoChosenDeleted(position : Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.product_add_title_dialog_delete_video)
        builder.setMessage(R.string.product_add_description_dialog_delete_video)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.label_delete, { dialog, id ->
            listener.onVideoChosenDeleted(adapter.data[position] as VideoViewModel)
            videoIDs.remove((adapter.data[position] as VideoViewModel).videoID)
            updateListData(adapter.data[position], DELETE_VIDEO_CHOSEN)
            (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
            showSnackbarGreen(getString(R.string.product_add_message_success_delete_video_choseen))
        })
        builder.setNegativeButton(R.string.label_cancel, { dialog, id -> dialog.cancel() })

        val alert = builder.create()
        alert.show()
    }

    override fun showSnackbarRed(message: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, message)
    }

    override fun showSnackbarGreen(message: String) {
        NetworkErrorHelper.showGreenCloseSnackbar(activity, message)
    }

    override fun setProductAddVideoFragmentListener(listener: ProductAddVideoFragment.Listener) {
        this.listener = listener
    }


    interface Listener {
        fun onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList : List<VideoRecommendationViewModel>)
        fun onVideoChosenDeleted(videoViewModel : VideoViewModel)
        fun onVideoChosenAdded(videoRecommendationViewModel : VideoRecommendationViewModel)
    }

    companion object {
        const val EXTRA_VIDEOS_LINKS = "KEY_VIDEOS_LINK"
        const val EXTRA_VIDEO_RECOMMENDATION = "KEY_VIDEO_RECOMMENDATION"

        const val MAX_VIDEO = 3
        const val MAX_VIDEO_RECOMMENDATION = 10

        const val REQUEST_CODE_GET_VIDEO_RECOMMENDATION = 1

        const val ADD_VIDEO_CHOSEN = 0
        const val DELETE_VIDEO_CHOSEN = 1

        fun createInstance(): Fragment {
            return ProductAddVideoFragment()
        }
    }
}
