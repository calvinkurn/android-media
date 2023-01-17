package com.tokopedia.usercomponents.userconsent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.databinding.FragmentDebugUserConsentBinding
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable

open class UserConsentDebugFragment: BaseDaggerFragment() {

    var viewBinding by autoClearedNullable<FragmentDebugUserConsentBinding>()

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun initInjector() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentDebugUserConsentBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.textCollextionId?.editText?.setText(COLLECTION_ID_SAMPLE)
        viewBinding?.buttonLoadConsent?.setOnClickListener {
            loadConsentComponent(
                viewBinding?.textCollextionId?.editText?.text.toString(),
                viewBinding?.textCollextionVersion?.editText?.text.toString()
            )
            viewBinding?.sampleUserConsent?.show()
            viewBinding?.textPayloadData?.hide()
        }
    }

    private fun loadConsentComponent(collectionId: String, version: String) {
        val consentParam = ConsentCollectionParam(
            collectionId = collectionId,
            version = version,
            identifier = "12345"
        )
        viewBinding?.sampleUserConsent?.apply {
        }?.load(viewLifecycleOwner, this, consentParam)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.sampleUserConsent?.onDestroy()
    }

    companion object {
        private const val SCREEN_NAME = "User Consent Debug Fragment"
        private const val COLLECTION_ID_SAMPLE = "bf7c9ba1-a4a9-447e-bbee-974c905a95ac"
    }
}
