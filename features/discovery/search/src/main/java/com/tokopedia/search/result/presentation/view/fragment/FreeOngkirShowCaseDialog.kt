package com.tokopedia.search.result.presentation.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
        @JvmStatic
        fun show(activity: FragmentActivity?, hasFreeOngkirBadge: Boolean) {
            try {
                if (activity == null) return

                tryShowFragment(activity, hasFreeOngkirBadge)
            }
            catch(e: Exception) {

            }
        }

        private fun tryShowFragment(activity: FragmentActivity, hasFreeOngkirBadge: Boolean) {
            if (isShowCaseHasNotShown(activity)
                    && hasFreeOngkirBadge) {
                val supportFragmentManager = activity.supportFragmentManager.beginTransaction()
                supportFragmentManager.add(R.id.rootSearchResult, FreeOngkirShowCaseDialog())
                supportFragmentManager.commit()
            }
        }

        private fun isShowCaseHasNotShown(activity: FragmentActivity): Boolean {
            val freeOngkirLocalCache = getFreeOngkirShowCaseLocalCache(activity)
            return !freeOngkirLocalCache.getBoolean(FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN)
        }

        private fun getFreeOngkirShowCaseLocalCache(activity: FragmentActivity): LocalCacheHandler {
            return LocalCacheHandler(activity, FREE_ONGKIR_LOCAL_CACHE_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_result_free_ongkir_show_case_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonOKFreeOngkir?.setOnClickListener { onClickButtonOK() }
    }

    private fun onClickButtonOK() {
        activity?.let { activity ->
            setFreeOngkirShowCaseAlreadyShown(activity)
            dismiss(activity)
        }
    }

    private fun setFreeOngkirShowCaseAlreadyShown(activity: FragmentActivity) {
        val freeOngkirLocalCache = getFreeOngkirShowCaseLocalCache(activity)
        freeOngkirLocalCache.putBoolean(FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
        freeOngkirLocalCache.applyEditor()
    }

    private fun dismiss(activity: FragmentActivity) {
        activity.supportFragmentManager.beginTransaction().remove(this).commit()
    }
}