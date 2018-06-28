package com.tokopedia.product.edit.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.networklib.util.RestClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.activity.ProductAddVideoRecommendationActivity
import com.tokopedia.product.edit.adapter.ProductAddVideoAdapterTypeFactory
import com.tokopedia.product.edit.listener.GetVideoRecommendationListener
import com.tokopedia.product.edit.listener.ProductAddVideoView
import com.tokopedia.product.edit.listener.SectionVideoRecommendationListener
import com.tokopedia.product.edit.listener.VideoChoosenListener
import com.tokopedia.product.edit.mapper.VideoMapper
import com.tokopedia.product.edit.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.presenter.ProductAddVideoPresenter
import com.tokopedia.product.edit.viewmodel.*

class ProductAddVideoFragment : BaseListFragment<ProductAddVideoBaseViewModel, ProductAddVideoAdapterTypeFactory>(), ProductAddVideoView, VideoChoosenListener, SectionVideoRecommendationListener {

    override val getVideoIDs: ArrayList<String> get() = videoIDs
    override val contextView: Context get() = activity

    var videoIDs: ArrayList<String> = ArrayList()
    private lateinit var productAddVideoPresenter : ProductAddVideoPresenter
    private lateinit var getVideoRecommendationListener : GetVideoRecommendationListener
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

        var btnTambah: Button = view.findViewById(R.id.button_tambah)

        btnTambah.setOnClickListener({
        })

        return view
    }

    override fun getAdapterTypeFactory(): ProductAddVideoAdapterTypeFactory {
        return ProductAddVideoAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: ProductAddVideoBaseViewModel?) {

    }

    override fun getScreenName(): String {
        return getString(R.string.title_activity_video)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        productAddVideoPresenter.getYoutubaDataVideoChoosen(videoIDs)
    }

    override fun renderListData(productAddVideoBaseViewModelList: List<ProductAddVideoBaseViewModel>) {
        renderList(productAddVideoBaseViewModelList)
    }

    private fun updateListData(productAddVideoBaseViewModel : ProductAddVideoBaseViewModel, type : Int){
        if(type == ADD_VIDEO_CHOOSEN) {
            for (i in 0 until adapter.data.size) {
                val productAddVideoBaseViewModels = adapter.data[i]
                if (productAddVideoBaseViewModels is EmptyVideoViewModel) {
                    adapter.clearElement(productAddVideoBaseViewModels)
                    val titleVideoChoosenViewModel = TitleVideoChoosenViewModel()
                    adapter.addElement(titleVideoChoosenViewModel)
                    break
                }
            }
            adapter.addElement(productAddVideoBaseViewModel)
        } else {
            adapter.clearElement(productAddVideoBaseViewModel)
            if(videoIDs.size == 1){
                for (i in 0 until adapter.data.size) {
                    val productAddVideoBaseViewModels = adapter.data[i]
                    if (productAddVideoBaseViewModels is TitleVideoChoosenViewModel) {
                        adapter.clearElement(productAddVideoBaseViewModels)
                        val emptyVideoViewModel = EmptyVideoViewModel()
                        adapter.addElement(emptyVideoViewModel)
                        break
                    }
                }
            }
        }
    }

    override fun onSuccessGetYoutubeDataVideoRecommendationFeatured(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        val mapper = VideoRecommendationMapper()
        getVideoRecommendationListener.onSuccessGetVideoRecommendationFeatured(mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList))
    }

    override fun onSuccessGetYoutubeDataVideoChoosen(youtubeVideoModelArrayList: ArrayList<YoutubeVideoModel>) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, ProductAddVideoFragment.MAX_VIDEO)
        productAddVideoPresenter.getVideoRecommendationFeatured("iphone", MAX_VIDEO_RECOMMENDATION_FEATURED)
    }

    override fun onErrorGetVideoData(e: Throwable) {

    }


    override fun setGetVideoRecommendationListener(listener: GetVideoRecommendationListener) {
        getVideoRecommendationListener = listener
    }

    override fun onShowMoreClicked() {
        val intent = Intent(activity, ProductAddVideoRecommendationActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_GET_VIDEO_RECOMMENDATION)
    }

    override fun onVideoRecommendationFeaturedClicked(videoRecommendationViewModel : VideoRecommendationViewModel) {
        if(videoIDs.contains(videoRecommendationViewModel.videoID)){
           Toast.makeText(activity, "sudah ad", Toast.LENGTH_LONG).show()
        } else {
            updateListData(mapper.transformVideoRecommendationViewModelToVideoViewModel(videoRecommendationViewModel), ADD_VIDEO_CHOOSEN)
            videoIDs.add(videoRecommendationViewModel.videoID!!)
            (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.product_from_to_video, videoIDs.size, MAX_VIDEO)
        }
    }

    override fun onVideoChoosenDeleted(videoViewModel: VideoViewModel) {

    }


    companion object {
        const val EXTRA_VIDEOS_LINKS = "KEY_VIDEOS_LINK"

        const val MAX_VIDEO = 3
        const val MAX_VIDEO_RECOMMENDATION_FEATURED = 4

        const val REQUEST_CODE_GET_VIDEO_RECOMMENDATION = 1

        const val ADD_VIDEO_CHOOSEN = 0
        const val DELETE_VIDEO_CHOOSEN = 1

        fun createInstance(): Fragment {
            return ProductAddVideoFragment()
        }
    }
}
