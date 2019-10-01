package com.tokopedia.search.result.presentation.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.constants.SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN
import com.tokopedia.search.R
import kotlinx.android.synthetic.main.search_result_free_ongkir_show_case_dialog.*

class FreeOngkirShowCaseDialog: Fragment() {

    companion object {
        @JvmField
        val TAG: String = FreeOngkirShowCaseDialog::class.java.simpleName

        @JvmStatic
        fun newInstance(): FreeOngkirShowCaseDialog {
            return FreeOngkirShowCaseDialog()
        }

        @JvmStatic
        fun show(activity: FragmentActivity) {
            if (shouldShowFreeOngkirShowCase(activity)) {
                val supportFragmentManager = activity.supportFragmentManager.beginTransaction()
                supportFragmentManager.add(newInstance(), TAG)
                supportFragmentManager.commit()
            }
        }

        private fun shouldShowFreeOngkirShowCase(activity: FragmentActivity): Boolean {
            val freeOngkirLocalCache = getFreeOngkirShowCaseLocalCache(activity)
            return !freeOngkirLocalCache.getBoolean(FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN)
        }

        private fun getFreeOngkirShowCaseLocalCache(activity: FragmentActivity): LocalCacheHandler {
            return LocalCacheHandler(activity, FREE_ONGKIR_LOCAL_CACHE_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_free_ongkir_show_case_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonOKFreeOngkir?.setOnClickListener { onClickButtonOK() }
    }

    private fun onClickButtonOK() {
        activity?.let { activity ->
            setFreeOngkirLocalCacheAlreadyShown(activity)
            dismiss(activity)
        }
    }

    private fun setFreeOngkirLocalCacheAlreadyShown(activity: FragmentActivity) {
        val freeOngkirLocalCache = getFreeOngkirShowCaseLocalCache(activity)
        freeOngkirLocalCache.putBoolean(FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
        freeOngkirLocalCache.applyEditor()
    }

    private fun dismiss(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().remove(this).commit()
    }
}