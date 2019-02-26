package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.activity.MediaPreviewActivity
import com.tokopedia.affiliate.feature.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.fragment_af_media_preview.*

/**
 * @author by milhamj on 25/02/19.
 */
class MediaPreviewFragment : BaseDaggerFragment() {

    private lateinit var viewModel: CreatePostViewModel

    private val imageAdapter: PostImageAdapter by lazy {
        PostImageAdapter()
    }
    private val productAdapter: RelatedProductAdapter by lazy {
        RelatedProductAdapter(null, RelatedProductAdapter.TYPE_PREVIEW)
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = MediaPreviewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_media_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
    }

    private fun initVar() {
        viewModel = arguments?.getParcelable(MediaPreviewActivity.CREATE_POST_MODEL)
                ?: CreatePostViewModel()
    }

    private fun initView() {
        imageAdapter.setList(viewModel.completeImageList)
        mediaViewPager.adapter = imageAdapter
        mediaViewPager.offscreenPageLimit = imageAdapter.count
        tabLayout.setupWithViewPager(mediaViewPager)

        val relatedProducts = ArrayList(viewModel.relatedProducts)
        productAdapter.setList(relatedProducts)
        productAdapter.removeEmpty()
        relatedProductRv.adapter = productAdapter
        relatedProductRv.setHasFixedSize(true)

        deleteMediaBtn.setOnClickListener {
            if (viewModel.mainImageIndex == tabLayout.selectedTabPosition
                    && tabLayout.selectedTabPosition == imageAdapter.count - 2
                    && tabLayout.selectedTabPosition - 1 > 0) {
                viewModel.mainImageIndex = tabLayout.selectedTabPosition - 1
            } else {
                viewModel.mainImageIndex = 0
            }

            if (tabLayout.selectedTabPosition < viewModel.fileImageList.size) {
                viewModel.fileImageList.removeAt(tabLayout.selectedTabPosition)
            } else {
                viewModel.urlImageList.removeAt(
                        tabLayout.selectedTabPosition - viewModel.fileImageList.size
                )
            }
            imageAdapter.imageList.removeAt(tabLayout.selectedTabPosition)
            imageAdapter.notifyDataSetChanged()

            if (imageAdapter.imageList.isEmpty()) {
                deleteMediaBtn.hide()
            }
        }
    }
}