package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.gson.Gson
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.Actions
import com.tokopedia.tokomember_seller_dashboard.model.TripleDotsItem
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPTION_MENU
import com.tokopedia.tokomember_seller_dashboard.util.CANCEL
import com.tokopedia.tokomember_seller_dashboard.util.DELETE
import com.tokopedia.tokomember_seller_dashboard.util.DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.SHARE
import com.tokopedia.tokomember_seller_dashboard.util.STOP
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.tm_dash_options_menu_bottomsheet.*

class TokomemberOptionsMenuBottomsheet: BottomSheetUnify() {

    private val childLayoutRes = R.layout.tm_dash_options_menu_bottomsheet
    private var tripleDotsList: List<TripleDotsItem?>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString(BUNDLE_OPTION_MENU)?.let {
            tripleDotsList = Gson().fromJson(it, Actions::class.java).tripleDots
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO get all triple dots type from product team
        tripleDotsList?.forEach {
            if(it?.type.equals(EXTEND)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLOCK) }
            }
            if(it?.type.equals(CANCEL)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLEAR) }
            }
            if(it?.type.equals(EDIT)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.EDIT) }
            }
            if(it?.type.equals(DELETE)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.DELETE) }
            }
            if(it?.type.equals(STOP)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.CLEAR) }
            }
            if(it?.type.equals(SHARE)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.SHARE) }
            }
            if(it?.type.equals(DUPLICATE)){
                it?.text?.let { it1 -> addOptionItem(it1, IconUnify.COPY) }
            }
        }

    }

    private fun addOptionItem(text: String, icon: Int) {

        val childLayout = LayoutInflater.from(requireContext()).inflate(R.layout.tm_dash_options_menu_item, container_options_menu ,false)
        childLayout.findViewById<ImageUnify>(R.id.icon_options_menu).setImageDrawable(getIconUnifyDrawable(childLayout.context, icon))
        childLayout.findViewById<Typography>(R.id.tv_options_menu).text = text
        container_options_menu.addView(childLayout)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true

        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        const val TAG = "TM_DASH_OPTIONS_MENU_BOTTOM_SHEET"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val tokomemberIntroBottomsheet = TokomemberOptionsMenuBottomsheet().apply {
                arguments = bundle
            }
            tokomemberIntroBottomsheet.show(childFragmentManager, TAG)
        }
    }

}