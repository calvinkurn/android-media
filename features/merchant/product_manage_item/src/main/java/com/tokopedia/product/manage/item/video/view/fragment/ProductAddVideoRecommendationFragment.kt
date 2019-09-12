package com.tokopedia.product.manage.item.video.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.view.adapter.ProductAddVideoRecommendationAdapterTypeFactory
import com.tokopedia.product.manage.item.video.view.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.manage.item.video.view.fragment.ProductAddVideoFragment.Companion.MAX_VIDEO
import com.tokopedia.product.manage.item.video.view.listener.VideoRecommendationListener
import com.tokopedia.product.manage.item.video.view.model.ProductAddVideoRecommendationBaseViewModel
import com.tokopedia.product.manage.item.video.view.model.TitleVideoRecommendationViewModel
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel
import kotlinx.android.synthetic.main.fragment_product_add_video_recommendation.*
import java.util.ArrayList

class ProductAddVideoRecommendationFragment : BaseListFragment<ProductAddVideoRecommendationBaseViewModel, ProductAddVideoRecommendationAdapterTypeFactory>(), ProductAddVideoRecommendationView, VideoRecommendationListener {

    override val contextView: Context get() = activity!!

    private var videoRecommendationViewModelList: ArrayList<VideoRecommendationViewModel> = ArrayList()
    private var videoIDs: ArrayList<String> = ArrayList()
    private var newVideoIDsRecommendation: ArrayList<String> = ArrayList()
    private var remainSlot: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        GraphqlClient.init(activity!!.applicationContext)

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_default)
        if(activity!!.intent != null){
            videoRecommendationViewModelList = activity!!.intent.getParcelableArrayListExtra<VideoRecommendationViewModel>(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION)
            videoIDs = activity!!.intent.getStringArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEOS_LINKS)
            remainSlot = (MAX_VIDEO - videoIDs.size)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_add_video_recommendation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSimpan.setOnClickListener({
            if(remainSlot == 0 && newVideoIDsRecommendation.size > 0) {
                showSnackbarRed(getString(R.string.product_add_message_slot_full_video_chosen))
            } else {
                if(newVideoIDsRecommendation.size <= remainSlot){
                    val intent = Intent()
                    intent.putParcelableArrayListExtra(ProductAddVideoFragment.EXTRA_VIDEO_RECOMMENDATION, videoRecommendationViewModelList)
                    activity!!.setResult(Activity.RESULT_OK, intent)
                    activity!!.finish()
                } else {
                    showSnackbarRed(getString(R.string.product_add_message_remain_slot_video_chosen, remainSlot))
                }
            }

        })
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

            if(!videoIDs.contains((adapter.data[position] as VideoRecommendationViewModel).videoID)){
                if( isChecked ) {
                    newVideoIDsRecommendation.add((adapter.data[position] as VideoRecommendationViewModel).videoID!!)
                } else {
                    newVideoIDsRecommendation.remove((adapter.data[position] as VideoRecommendationViewModel).videoID!!)
                }
            } else {
                if(isChecked){
                    remainSlot--
                } else {
                    remainSlot++
                }
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

        fun createInstance(): androidx.fragment.app.Fragment {
            return ProductAddVideoRecommendationFragment()
        }
    }
}