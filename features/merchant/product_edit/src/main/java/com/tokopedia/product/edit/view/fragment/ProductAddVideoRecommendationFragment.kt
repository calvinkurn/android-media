package com.tokopedia.product.edit.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory
import com.tokopedia.product.edit.view.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.edit.domain.mapper.VideoRecommendationMapper
import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.fragment.ProductAddVideoFragment.Companion.MAX_VIDEO
import com.tokopedia.product.edit.view.listener.VideoRecommendationListener
import com.tokopedia.product.edit.view.presenter.ProductAddVideoRecommendationPresenter
import com.tokopedia.product.edit.view.viewmodel.ProductAddVideoRecommendationBaseViewModel
import com.tokopedia.product.edit.view.viewmodel.TitleVideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel
import java.util.ArrayList

class ProductAddVideoRecommendationFragment : BaseListFragment<ProductAddVideoRecommendationBaseViewModel, ProductAddVideoRecommendationAdapterTypeFactory>(), ProductAddVideoRecommendationView, VideoRecommendationListener {

    override val contextView: Context get() = activity

    private var videoRecommendationViewModelList: ArrayList<VideoRecommendationViewModel> = ArrayList()
    private var videoIDs: ArrayList<String> = ArrayList()

    private lateinit var productAddVideoRecommendationPresenter: ProductAddVideoRecommendationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        GraphqlClient.init(activity.applicationContext)
        productAddVideoRecommendationPresenter = ProductAddVideoRecommendationPresenter()
        productAddVideoRecommendationPresenter.attachView(this)

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_default);
        if(activity.intent != null){
            videoRecommendationViewModelList = activity.intent.getParcelableArrayListExtra<VideoRecommendationViewModel>(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION)
            videoIDs = activity.intent.getStringArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEOS_LINKS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_product_add_video_recommendation, container, false)

        val btnSimpan: Button = view.findViewById(R.id.button_simpan)

        btnSimpan.setOnClickListener({

            if(videoIDs.size <= MAX_VIDEO) {
                val intent = Intent()
                intent.putParcelableArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION, videoRecommendationViewModelList)
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            } else {
                showSnackbarRed(getString(R.string.product_add_message_slot_full_video_chosen))
            }


//            if((videoIDs.size - countOldVideoRecommendationChosen) + countNewVideoRecommendationChosen < MAX_VIDEO){
//                val remainSlot = MAX_VIDEO - videoIDs.size
//                if(remainSlot >= countNewVideoRecommendationChosen ){
//                    val intent = Intent()
//                    intent.putParcelableArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION, videoRecommendationViewModelList)
//                    activity.setResult(Activity.RESULT_OK, intent)
//                    activity.finish()
//                } else {
//                    showSnackbarRed(getString(R.string.product_add_message_remain_slot_video_chosen, remainSlot))
//                }
//            } else {
//                showSnackbarRed(getString(R.string.product_add_message_slot_full_video_chosen))
//            }
        })

        return view
    }

    override fun getAdapterTypeFactory(): ProductAddVideoRecommendationAdapterTypeFactory {
        return ProductAddVideoRecommendationAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: ProductAddVideoRecommendationBaseViewModel?) {

    }

    override fun getScreenName(): String {
        return getString(R.string.title_activity_video_recommendation)
    }

    override fun initInjector() {}

    override fun loadData(page: Int) {
        val productAddVideoRecommendationBaseViewModel : ArrayList<ProductAddVideoRecommendationBaseViewModel> = ArrayList()
        productAddVideoRecommendationBaseViewModel.addAll(videoRecommendationViewModelList)
        val titleVideoRecommendationViewModel = TitleVideoRecommendationViewModel()
        productAddVideoRecommendationBaseViewModel.add(0, titleVideoRecommendationViewModel)

        renderList(productAddVideoRecommendationBaseViewModel)
    }

    override fun onCheckboxClicked(position: Int, isChecked: Boolean) {
        if(adapter.data[position] is VideoRecommendationViewModel){
            (adapter.data[position] as VideoRecommendationViewModel).chosen = isChecked
            if( isChecked ) {
                if(!videoIDs.contains((adapter.data[position] as VideoRecommendationViewModel).videoID))
                    videoIDs.add((adapter.data[position] as VideoRecommendationViewModel).videoID!!)
            } else {
                videoIDs.remove((adapter.data[position] as VideoRecommendationViewModel).videoID!!)
            }
        }
    }

    override fun showSnackbarRed(message: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, message)
    }

    override fun showSnackbarGreen(message: String) {
        NetworkErrorHelper.showGreenCloseSnackbar(activity, message)
    }

    companion object {

        fun createInstance(): android.support.v4.app.Fragment {
            return ProductAddVideoRecommendationFragment()
        }
    }
}
