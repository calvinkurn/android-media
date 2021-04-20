package com.tokopedia.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.view.viewmodel.MediaType
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.fragment_af_media_preview.*


/**
 * @author by milhamj on 25/02/19.
 */
class CreatePostMediaPreviewFragment : BaseDaggerFragment() {

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

    private val deleteDialog: Dialog by lazy {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.cp_update_post))
        dialog.setDesc(getString(R.string.cp_delete_warning_desc))
        dialog.setBtnOk(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
        dialog.setBtnCancel(getString(com.tokopedia.design.R.string.title_delete))
        dialog.setOnOkClickListener{
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener{
            dialog.dismiss()
            doDeleteMedia()
        }
        dialog.setCancelable(true)
        dialog
    }

    private val imageList: ArrayList<String>
        get() = ArrayList(viewModel.completeImageList.map { it.path })

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = CreatePostMediaPreviewFragment()
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
        if (viewModel.completeImageList.firstOrNull()?.type == MediaType.VIDEO) {
            viewFirstPage.hide()
            tabLayout.hide()
            btnPlay.show()
            imageAdapter.setList(imageList, PostImageAdapter.VIDEO)
        } else {
            imageAdapter.setList(imageList)
        }

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
            if (viewModel.completeImageList.size == 1) {
                deleteDialog.show()
            } else {
                doDeleteMedia()
            }
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
            imageAdapter.setList(imageList)
            mediaViewPager.currentItem = 0

            updateMainImageText()
            updateResultIntent()
        }

        updateMainImageText()
        updateDeleteBtn()
    }

    private fun doDeleteMedia() {
        if (tabLayout.selectedTabPosition < viewModel.fileImageList.size) {
            viewModel.fileImageList.removeAt(tabLayout.selectedTabPosition)
        } else {
            viewModel.urlImageList.removeAt(
                    tabLayout.selectedTabPosition - viewModel.fileImageList.size
            )
        }
        imageAdapter.setList(ArrayList(viewModel.completeImageList.map { it.path }))

        updateDeleteBtn()
        updateResultIntent()
    }

    private fun updateDeleteBtn() {
        deleteMediaBtn.showWithCondition(viewModel.fileImageList.isNotEmpty())
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
            mainImageText.setText(R.string.cp_main_image)
            mainImageText.setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.black_38))
            mainImageIcon.show()
        } else {
            mainImageText.setText(R.string.cp_set_main_image)
            mainImageText.setTextColor(MethodChecker.getColor(context, com.tokopedia.design.R.color.medium_green))
            mainImageIcon.hide()
        }
    }
}
