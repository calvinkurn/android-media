package com.tokopedia.product.edit.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.activity.ProductAddVideoRecommendationActivity
import com.tokopedia.product.edit.view.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.edit.view.listener.ProductAddVideoView
import com.tokopedia.product.edit.view.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.view.listener.VideoChosenListener
import com.tokopedia.product.edit.domain.mapper.VideoMapper
import com.tokopedia.product.edit.domain.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.util.YoutubeUtil
import com.tokopedia.product.edit.view.presenter.ProductAddVideoPresenter
import com.tokopedia.product.edit.view.viewmodel.*
import kotlinx.android.synthetic.main.fragment_product_add_video.*

class ProductAddVideoFragment : BaseListFragment<ProductAddVideoBaseViewModel, ProductAddVideoAdapterTypeFactory>(), ProductAddVideoView, VideoChosenListener, SectionVideoRecommendationListener {

    override val contextView: Context get() = activity!!

    var videoIDs: ArrayList<String> = ArrayList()
    lateinit var productName: String
    private var videoRecommendationViewModelList: ArrayList<VideoRecommendationViewModel> = ArrayList()
    private lateinit var productAddVideoPresenter : ProductAddVideoPresenter
    private var listener : Listener? = null
    private val mapper = VideoMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        GraphqlClient.init(activity!!.applicationContext)
        NetworkClient.init(activity!!.applicationContext)

        productAddVideoPresenter = ProductAddVideoPresenter()
        productAddVideoPresenter.attachView(this)

        if(activity!!.intent != null){
            videoIDs = activity!!.intent.getStringArrayListExtra(EXTRA_VIDEOS_LINKS)
            productName = activity!!.intent.getStringExtra(EXTRA_KEYWORD)
            productAddVideoPresenter.setProductName(productName)
        }
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_VIDEO_RECOMMENDATION -> {
                    videoRecommendationViewModelList = data!!.getParcelableArrayListExtra<VideoRecommendationViewModel>(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION)
                    val videoViewModelList = adapter.data
                    for(videoViewModel in videoViewModelList){
                        if(videoViewModel is VideoViewModel){
                            for(videoRecommendationViewModel in videoRecommendationViewModelList){
                                if(videoRecommendationViewModel.videoID == videoViewModel.videoID){
                                    deleteVideoChosenFromList(videoViewModel)
                                }
                            }
                        }
                    }
                    for(videoRecommendationViewModel in videoRecommendationViewModelList){
                        if(videoRecommendationViewModel.chosen){
                            addVideoChosenToList(transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel))
                        }
                    }
                    listener?.onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList)
                    showSnackbarGreen(getString(R.string.product_add_message_success_change_video_recommendation))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTambah.setOnClickListener({
            showDialogAddVideoChosenFromUrl()
        })
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
        productAddVideoPresenter.getYoutubeDataVideoChosen(videoIDs)
        setButtonAddVideoUrl()
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
        setButtonAddVideoUrl()
    }

    private fun isVideoChosenSlotNotFull(): Boolean {
        if(videoIDs.size >= MAX_VIDEO) {
            showSnackbarRed(getString(R.string.product_add_message_slot_full_video_chosen))
        }
        return videoIDs.size < MAX_VIDEO
    }

    private fun setButtonAddVideoUrl(){
        if(videoIDs.size < MAX_VIDEO) {
            btnTambah.visibility = View.VISIBLE
        } else {
            btnTambah.visibility = View.GONE
        }
    }

    override fun addVideoIDfromURL(videoID: String) {
        for(id in videoIDs){
            if(id == videoID){
                showSnackbarRed(getString(R.string.product_add_message_exist_video_chosen))
                return
            }
        }
        productAddVideoPresenter.getYoutubaDataVideoUrl(videoID)
    }

    override fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        val mapper = VideoRecommendationMapper()
        videoRecommendationViewModelList = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList) as ArrayList<VideoRecommendationViewModel>
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
        listener?.onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList)
    }

    override fun onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, ProductAddVideoFragment.MAX_VIDEO)
        if(!productName.isEmpty())
            productAddVideoPresenter.getVideoRecommendation(productName, MAX_VIDEO_RECOMMENDATION)
    }

    override fun onSuccessGetYoutubeDataVideoUrl(youtubeVideoModel: YoutubeVideoModel) {
        addVideoChosenToList(mapper.transformDataToVideoViewModel(youtubeVideoModel))
        showSnackbarGreen(getString(R.string.product_add_message_success_add_video_chosen))
    }

    override fun onErrorGetVideoData(e: Throwable) {
        NetworkErrorHelper.createSnackbarWithAction(activity) {

        }.showRetrySnackbar()
    }


    override fun onShowMoreClicked() {
        val intent = Intent(activity, ProductAddVideoRecommendationActivity::class.java)
        intent.putParcelableArrayListExtra(EXTRA_VIDEO_RECOMMENDATION, videoRecommendationViewModelList)
        intent.putStringArrayListExtra(EXTRA_VIDEOS_LINKS, videoIDs)
        startActivityForResult(intent, REQUEST_CODE_GET_VIDEO_RECOMMENDATION)
    }

    override fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
        YoutubeUtil.playYoutubeVideo(context!!, videoRecommendationViewModel.videoID!!)

    }

    override fun onVideoRecommendationPlusClicked(videoRecommendationViewModel: VideoRecommendationViewModel) {
        if (videoIDs.contains(videoRecommendationViewModel.videoID)) {
            for (i in 0 until adapter.dataSize ){
                if(adapter.data[i] is VideoViewModel && (adapter.data[i] as VideoViewModel).videoID == videoRecommendationViewModel.videoID){
                    showDialogDeleteVideoChosen(adapter.data[i] as VideoViewModel)
                    break
                }
            }
        } else {
            if(isVideoChosenSlotNotFull()) {
                showDialogAddVideoChosenFromFeatured(transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel))
            }
        }
    }

    override fun onVideoChosenClicked(position: Int) {
        val videoID = (adapter.data[position] as VideoViewModel).videoID
        YoutubeUtil.playYoutubeVideo(context!!, videoID!!)
    }

    override fun onVideoChosenDeleteClicked(position : Int) {
       showDialogDeleteVideoChosen(adapter.data[position] as VideoViewModel)
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

    private fun showDialogAddVideoChosenFromUrl() {
        val dialog = ProductAddVideoDialogFragment()
        dialog.show(fragmentManager, ProductAddVideoDialogFragment.TAG)
    }

    private fun showDialogAddVideoChosenFromFeatured(videoViewModel: VideoViewModel) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.product_add_title_dialog_add_video)
        builder.setMessage(R.string.product_add_description_dialog_add_video)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.label_add, { dialog, id ->
            setVideoRecommendationChosen(videoViewModel.videoID, true)
            listener?.notifyVideoChanged()
            addVideoChosenToList(videoViewModel)
            showSnackbarGreen(getString(R.string.product_add_message_success_add_video_recommendation))
        })
        builder.setNegativeButton(R.string.label_cancel, { dialog, id -> dialog.cancel() })

        val alert = builder.create()
        alert.show()
    }

    private fun showDialogDeleteVideoChosen(videoViewModel: VideoViewModel) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.product_add_title_dialog_delete_video)
        builder.setMessage(R.string.product_add_description_dialog_delete_video)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.label_delete, { dialog, id ->
            setVideoRecommendationChosen(videoViewModel.videoID, false)
            deleteVideoChosenFromList(videoViewModel)
            listener?.notifyVideoChanged()
            showSnackbarGreen(getString(R.string.product_add_message_success_delete_video_chosen))
        })
        builder.setNegativeButton(R.string.label_cancel, { dialog, id -> dialog.cancel() })

        val alert = builder.create()
        alert.show()
    }

    private fun setVideoRecommendationChosen(videoId: String?, isChoosen: Boolean){
        for(videoRecommendationViewModel in videoRecommendationViewModelList){
            if(videoRecommendationViewModel.videoID == videoId){
                videoRecommendationViewModel.chosen = isChoosen
            }
        }
    }

    private fun addVideoChosenToList(videoViewModel: VideoViewModel) {
        videoIDs.add(videoViewModel.videoID!!)
        updateListData(videoViewModel, ADD_VIDEO_CHOSEN)
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    private fun deleteVideoChosenFromList(videoViewModel: VideoViewModel) {
        videoIDs.remove(videoViewModel.videoID)
        updateListData(videoViewModel, DELETE_VIDEO_CHOSEN)
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    private fun transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel : VideoRecommendationViewModel): VideoViewModel {
        val videoViewModel = VideoViewModel()
        videoViewModel.videoID = videoRecommendationViewModel.videoID
        videoViewModel.snippetTitle = videoRecommendationViewModel.snippetTitle
        videoViewModel.snippetDescription = videoRecommendationViewModel.snippetDescription
        videoViewModel.thumbnailUrl = videoRecommendationViewModel.thumbnailUrl
        videoViewModel.snippetChannel = videoRecommendationViewModel.snippetChannel
        videoViewModel.duration = videoRecommendationViewModel.duration
        videoViewModel.recommendation = true
        return videoViewModel
    }

    interface Listener {
        fun onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList : List<VideoRecommendationViewModel>)
        fun notifyVideoChanged()
    }

    companion object {
        const val EXTRA_VIDEOS_LINKS = "KEY_VIDEOS_LINK"
        const val EXTRA_KEYWORD = "KEY_KEYWORD"
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
