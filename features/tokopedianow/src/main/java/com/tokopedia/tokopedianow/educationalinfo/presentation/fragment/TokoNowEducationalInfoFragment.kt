package com.tokopedia.tokopedianow.educationalinfo.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics
import com.tokopedia.tokopedianow.educationalinfo.di.component.DaggerEducationalInfoComponent
import com.tokopedia.tokopedianow.educationalinfo.presentation.bottomsheet.TokoNowEducationalInfoBottomSheet
import com.tokopedia.tokopedianow.educationalinfo.presentation.listener.EducationalInfoBottomSheetCallback
import com.tokopedia.tokopedianow.home.di.component.DaggerHomeComponent
import javax.inject.Inject

class TokoNowEducationalInfoFragment: Fragment() {

    companion object {
        const val KEY_SOURCE = "source"
        const val KEY_CHANNEL_ID = "channel_id"
        const val KEY_STATE = "state"

        fun newInstance(): TokoNowEducationalInfoFragment {
            return TokoNowEducationalInfoFragment()
        }
    }

    @Inject
    lateinit var analytics: EducationalInfoAnalytics

    private var source: String? = null
    private var channelId: String? = null
    private var state: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_education_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUriData()
        showEducationalInfoBottomSheet()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun initInjector() {
        DaggerEducationalInfoComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setUriData() {
        activity?.intent?.data?.let {
            source = getSourceParameter(it)
            channelId = getChannelIdParameter(it)
            state = getStateParameter(it)
        }
    }

    private fun showEducationalInfoBottomSheet() {
        val bottomSheet = TokoNowEducationalInfoBottomSheet.newInstance()
        bottomSheet.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet.source = source
        val listener = createEducationalInfoBottomSheetCallback()
        bottomSheet.show(childFragmentManager, listener)
    }

    private fun createEducationalInfoBottomSheetCallback() = if (channelId.isNullOrBlank() && state.isNullOrBlank()) {
        null
    } else {
        EducationalInfoBottomSheetCallback(
            analytics = analytics,
            channelId = channelId,
            state = state?.lowercase()
        )
    }

    private fun getSourceParameter(uri: Uri): String? {
        return uri.getQueryParameter(KEY_SOURCE)?.toBlankOrString()
    }

    private fun getChannelIdParameter(uri: Uri): String? {
        return uri.getQueryParameter(KEY_CHANNEL_ID)?.toBlankOrString()
    }

    private fun getStateParameter(uri: Uri): String? {
        return uri.getQueryParameter(KEY_STATE)?.toBlankOrString()
    }
}