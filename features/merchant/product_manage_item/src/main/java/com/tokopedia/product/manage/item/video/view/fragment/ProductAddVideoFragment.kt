package com.tokopedia.product.manage.item.video.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.manage.item.video.view.listener.ProductAddVideoView
import com.tokopedia.product.manage.item.video.view.listener.SectionVideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.listener.VideoChosenListener
import com.tokopedia.product.manage.item.video.domain.mapper.VideoMapper
import com.tokopedia.product.manage.item.video.domain.mapper.VideoRecommendationMapper
import com.tokopedia.product.manage.item.utils.YoutubeUtil
import com.tokopedia.product.manage.item.video.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.manage.item.video.view.activity.ProductAddVideoRecommendationActivity
import com.tokopedia.product.manage.item.video.view.model.*
import com.tokopedia.product.manage.item.video.view.presenter.ProductAddVideoPresenter
import kotlinx.android.synthetic.main.fragment_product_add_video.*

class ProductAddVideoFragment : BaseListFragment<ProductAddVideoBaseViewModel, ProductAddVideoAdapterTypeFactory>(), ProductAddVideoView, VideoChosenListener, SectionVideoRecommendationListener {

    override val contextView: Context get() = activity!!

    var videoIDs: ArrayList<String> = ArrayList()
    lateinit var productName: String
    private var videoViewModelList: ArrayList<VideoViewModel> = ArrayList()
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
        }
        if (savedInstanceState != null) {
            videoIDs = savedInstanceState.getStringArrayList(EXTRA_VIDEOS_LINKS)
            videoViewModelList = savedInstanceState.getParcelableArrayList<VideoViewModel>(EXTRA_VIDEO_CHOSEN)
            videoRecommendationViewModelList = savedInstanceState.getParcelableArrayList<VideoRecommendationViewModel>(EXTRA_VIDEO_RECOMMENDATION)
        }

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_VIDEO_RECOMMENDATION -> {
                    videoRecommendationViewModelList = data!!.getParcelableArrayListExtra<VideoRecommendationViewModel>(EXTRA_VIDEO_RECOMMENDATION)
                    adapter.data.filter { it is VideoViewModel && it.recommendation == true }.map {
                        deleteVideoChosenFromList(it as VideoViewModel)
                    }
                    videoRecommendationViewModelList.filter { it.chosen }.map {
                        addVideoChosenToList(transformVideoRecommendationViewModelToVideoViewModel(it))
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

    override fun onDestroy() {
        super.onDestroy()
        productAddVideoPresenter.detachView()
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
        if(videoRecommendationViewModelList.isEmpty())
            productAddVideoPresenter.getYoutubeDataVideoChosen(videoIDs)
        else {
            renderListData(videoViewModelList)
        }

        setButtonAddVideoUrl()
    }

    private fun renderListData(VideoViewModelList: List<VideoViewModel>) {
        adapter.clearAllElements()
        val productAddVideoBaseViewModelList : ArrayList<ProductAddVideoBaseViewModel> = ArrayList()
        if(!VideoViewModelList.isEmpty()){
            productAddVideoBaseViewModelList.addAll(VideoViewModelList)
            val titleVideoChosenViewModel = TitleVideoChosenViewModel()
            productAddVideoBaseViewModelList.add(0, titleVideoChosenViewModel)
        } else {
            val emptyVideoViewModel = EmptyVideoViewModel()
            productAddVideoBaseViewModelList.add(0, emptyVideoViewModel)
        }
        if(!productName.isEmpty()){
            val sectionVideoRecommendationViewModel = SectionVideoRecommendationViewModel()
            productAddVideoBaseViewModelList.add(0, sectionVideoRecommendationViewModel)
        }
        renderList(productAddVideoBaseViewModelList)
    }

    private fun updateListData(productAddVideoBaseViewModel : ProductAddVideoBaseViewModel, type : Int){
        if(type == ADD_VIDEO_CHOSEN) {
            adapter.data.filter { it is EmptyVideoViewModel }.map {
                adapter.clearElement(it)
                val titleVideoChosenViewModel = TitleVideoChosenViewModel()
                adapter.addElement(titleVideoChosenViewModel)
            }
            adapter.addElement(productAddVideoBaseViewModel)
        } else {
            adapter.clearElement(productAddVideoBaseViewModel)
            if(videoIDs.size == 0){
                adapter.data.filter { it is TitleVideoChosenViewModel }.map {
                    adapter.clearElement(it)
                    val emptyVideoViewModel = EmptyVideoViewModel()
                    adapter.addElement(emptyVideoViewModel)
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
        videoIDs.filter { it == videoID }.map {
            showSnackbarRed(getString(R.string.product_add_message_exist_video_chosen))
        }
        productAddVideoPresenter.getYoutubaDataVideoUrl(videoID)
    }

    override fun onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        val mapper = VideoRecommendationMapper()
        videoRecommendationViewModelList = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList) as ArrayList<VideoRecommendationViewModel>
        adapter.data.filter { it is VideoViewModel }.map {
            it as VideoViewModel
            videoRecommendationViewModelList.map{ video ->
                if(video.videoID == it.videoID){
                    video.chosen = true
                    it.recommendation = true
                }
            }
        }
        adapter.notifyDataSetChanged()
        listener?.onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList)
    }

    override fun onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        videoViewModelList.clear()
        videoViewModelList = mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList) as ArrayList<VideoViewModel>
        renderListData(videoViewModelList)
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
        if(!productName.isEmpty())
            productAddVideoPresenter.getVideoRecommendation(productName, MAX_VIDEO_RECOMMENDATION)
    }

    override fun onSuccessGetYoutubeDataVideoUrl(youtubeVideoModel: YoutubeVideoModel) {
        addVideoChosenToList(mapper.transformDataToVideoViewModel(youtubeVideoModel))
        showSnackbarGreen(getString(R.string.product_add_message_success_add_video_chosen))
    }

    override fun onEmptyGetVideoRecommendation() {
        if(adapter.data[0] is SectionVideoRecommendationViewModel){
            adapter.clearElement(adapter.data[0])
        }
        showSnackbarGreen(getString(R.string.product_add_message_empty_video_recommendation))
    }

    override fun onErrorGetVideoData(e: Throwable) {
        NetworkErrorHelper.createSnackbarWithAction(activity) {
            loadData(0)
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
            adapter.data.filter { it is VideoViewModel && it.videoID == videoRecommendationViewModel.videoID }.map{
                showDialogDeleteVideoChosen(it as VideoViewModel)
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

    override fun setProductAddVideoFragmentListener(listener: Listener) {
        this.listener = listener
        if(!videoRecommendationViewModelList.isEmpty())
            listener.onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList)
    }

    private fun showDialogAddVideoChosenFromUrl() {
        val dialog = ProductAddVideoDialogFragment()
        fragmentManager?.run {
            dialog.show(this, ProductAddVideoDialogFragment.TAG)
        }
    }

    private fun showDialogAddVideoChosenFromFeatured(videoViewModel: VideoViewModel) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.product_add_title_dialog_add_video)
        builder.setMessage(R.string.product_add_description_dialog_add_video)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.label_add, { _, _ ->
            setVideoRecommendationChosen(videoViewModel.videoID, true)
            listener?.notifyVideoChanged()
            addVideoChosenToList(videoViewModel)
            showSnackbarGreen(getString(R.string.product_add_message_success_add_video_recommendation))
        })
        builder.setNegativeButton(R.string.label_cancel, { dialog, _ -> dialog.cancel() })

        val alert = builder.create()
        alert.show()
    }

    private fun showDialogDeleteVideoChosen(videoViewModel: VideoViewModel) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(R.string.product_add_title_dialog_delete_video)
        builder.setMessage(R.string.product_add_description_dialog_delete_video)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.label_delete, { _, _ ->
            setVideoRecommendationChosen(videoViewModel.videoID, false)
            deleteVideoChosenFromList(videoViewModel)
            listener?.notifyVideoChanged()
            showSnackbarGreen(getString(R.string.product_add_message_success_delete_video_chosen))
        })
        builder.setNegativeButton(R.string.label_cancel, { dialog, _ -> dialog.cancel() })

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
        videoViewModelList.add(videoViewModel)
        updateListData(videoViewModel, ADD_VIDEO_CHOSEN)
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
    }

    private fun deleteVideoChosenFromList(videoViewModel: VideoViewModel) {
        videoIDs.remove(videoViewModel.videoID)
        videoViewModelList.remove(videoViewModel)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(EXTRA_VIDEOS_LINKS, videoIDs)
        outState.putParcelableArrayList(EXTRA_VIDEO_CHOSEN, videoViewModelList)
        outState.putParcelableArrayList(EXTRA_VIDEO_RECOMMENDATION, videoRecommendationViewModelList)
    }

    interface Listener {
        fun onSuccessGetYoutubeDataVideoRecommendation(videoRecommendationViewModelList : List<VideoRecommendationViewModel>)
        fun notifyVideoChanged()
    }

    companion object {
        const val EXTRA_VIDEOS_LINKS = "KEY_VIDEOS_LINK"
        const val EXTRA_KEYWORD = "KEY_KEYWORD"
        const val EXTRA_VIDEO_CHOSEN = "KEY_VIDEO_CHOSEN"
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