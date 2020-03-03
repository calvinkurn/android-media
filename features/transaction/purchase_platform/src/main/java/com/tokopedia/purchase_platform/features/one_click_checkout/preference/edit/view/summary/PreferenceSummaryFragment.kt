package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity

class PreferenceSummaryFragment : BaseDaggerFragment() {

    companion object {

        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): PreferenceSummaryFragment {
            val preferenceSummaryFragment = PreferenceSummaryFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            preferenceSummaryFragment.arguments = bundle
            return preferenceSummaryFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preference_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
    }

    private fun initHeader() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.hideAddButton()
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.showDeleteButton()
                parent.setDeleteButtonOnClickListener {
                    //do delete
                }
                parent.setHeaderTitle("Atur pilihan 1")
                parent.hideStepper()
            } else {
                parent.hideDeleteButton()
                parent.setHeaderTitle("Atur Express Checkout")
                parent.setHeaderSubtitle("Cek ringkasan pengaturanmu")
                parent.showStepper()
                parent.setStepperValue(100, true)
            }
        }
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

}