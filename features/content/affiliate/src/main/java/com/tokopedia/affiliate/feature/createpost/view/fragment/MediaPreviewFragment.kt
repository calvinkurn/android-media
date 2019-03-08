package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
    private val resultIntent: Intent by lazy {
        Intent()
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
        viewModel = arguments?.getParcelable(CreatePostViewModel.TAG) ?: CreatePostViewModel()
    }

    private fun initView() {
        imageAdapter.setList(viewModel.completeImageList)
        mediaViewPager.adapter = imageAdapter
        mediaViewPager.offscreenPageLimit = imageAdapter.count
        tabLayout.setupWithViewPager(mediaViewPager)
        tabLayout.onTabSelected {
            updateMainImageText()
        }

        val relatedProducts = ArrayList(viewModel.relatedProducts)
        productAdapter.setList(relatedProducts)
        productAdapter.removeEmpty()
        relatedProductRv.adapter = productAdapter
        relatedProductRv.setHasFixedSize(true)

        deleteMediaBtn.setOnClickListener {
            if (tabLayout.selectedTabPosition < viewModel.fileImageList.size) {
                viewModel.fileImageList.removeAt(tabLayout.selectedTabPosition)
            } else {
                viewModel.urlImageList.removeAt(
                        tabLayout.selectedTabPosition - viewModel.fileImageList.size
                )
            }
            imageAdapter.setList(viewModel.completeImageList)

            updateDeleteBtn()
            updateResultIntent()
        }

        mainImageText.setOnClickListener {
            if (tabLayout.selectedTabPosition < viewModel.fileImageList.size) {
                val image = viewModel.fileImageList[tabLayout.selectedTabPosition]
                viewModel.fileImageList.removeAt(tabLayout.selectedTabPosition)
                viewModel.fileImageList.add(0, image)
            } else {
                val image = viewModel.urlImageList[tabLayout.selectedTabPosition]
                viewModel.urlImageList.removeAt(tabLayout.selectedTabPosition)
                viewModel.urlImageList.add(0, image)
            }
            imageAdapter.setList(viewModel.completeImageList)

            updateMainImageText()
            updateResultIntent()
        }

        updateMainImageText()
        updateDeleteBtn()
    }

    private fun updateDeleteBtn() {
        deleteMediaBtn.showWithCondition(imageAdapter.imageList.isNotEmpty())
    }

    private fun updateResultIntent() {
        resultIntent.putExtra(CreatePostViewModel.TAG, viewModel)
        activity?.let {
            it.setResult(Activity.RESULT_OK, resultIntent)

            if (viewModel.completeImageList.isEmpty()) {
                it.finish()
            }
        }
    }

    private fun updateMainImageText() {
        if (tabLayout.selectedTabPosition == 0) {
            mainImageText.setText(R.string.af_main_image)
            mainImageText.setTextColor(MethodChecker.getColor(context, R.color.black_38))
            mainImageIcon.show()
        } else {
            mainImageText.setText(R.string.af_set_main_image)
            mainImageText.setTextColor(MethodChecker.getColor(context, R.color.medium_green))
            mainImageIcon.hide()
        }
    }
}