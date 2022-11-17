package com.tokopedia.contactus.inboxticket2.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.contactus.R
import com.tokopedia.csat_rating.di.component.DaggerCsatComponent
import com.tokopedia.csat_rating.di.general.CsatComponentCommon
import com.tokopedia.csat_rating.di.module.CsatRatingModule
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.unifycomponents.BottomSheetUnify

class ContactUsProvideRatingFragment: BaseFragmentProvideRating(){

    private val bottomSheetPage = BottomSheetUnify()


    companion object {
        fun newInstance(bundle: Bundle?): ContactUsProvideRatingFragment2 {
            val fragment = ContactUsProvideRatingFragment2()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val viewBottomSheetPage = initBottomSheet()

        bottomSheetPage.apply {
            showCloseIcon = false
            setChild(viewBottomSheetPage)
            showKnob = true
            clearContentPadding = true
            isFullpage = true
        }

        parentFragmentManager.let {
            bottomSheetPage.show(it, "")
        }
    }

    override fun initInjector() {
        DaggerCsatComponent.builder()
            .csatRatingModule(CsatRatingModule())
            .csatComponentCommon(getComponent(CsatComponentCommon::class.java))
            .build()
            .inject(this)
    }

    private fun initBottomSheet(): View? {
        val view = View.inflate(context, R.layout.bottom_sheet_rating_provide, null).apply {
            initViews(this)
        }

        return view
    }

    private var title: com.tokopedia.unifyprinciples.Typography? = null
    private var smileLayout: LinearLayout? = null

    private fun initViews(view : View) {
        title = view.findViewById(R.id.rating_txt_help_title)
        smileLayout = view.findViewById(R.id.smile_layout)
        title?.text = viewModel?.csatTitle
    //    initObserver(smileLayout!!)
    }

}
