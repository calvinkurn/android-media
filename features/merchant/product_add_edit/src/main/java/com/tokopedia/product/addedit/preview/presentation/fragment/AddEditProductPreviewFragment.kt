package com.tokopedia.product.addedit.preview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet

class AddEditProductPreviewFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance(): Fragment {
            return AddEditProductPreviewFragment()
        }

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addProductPhotoSection: ViewGroup = view.findViewById(R.id.add_product_photo_section)
        addProductPhotoSection.setOnClickListener {
            showPhotoTips()
        }
    }

    private fun showPhotoTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<ImageTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_photo_tips)
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_1), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_2), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_3), TEST_IMAGE_URL))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

}
