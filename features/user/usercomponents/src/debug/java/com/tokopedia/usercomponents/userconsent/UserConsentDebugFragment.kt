package com.tokopedia.usercomponents.userconsent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
        viewBinding?.buttonAction?.setOnClickListener {
            viewBinding?.sampleUserConsent?.submitConsent()
        }
        viewBinding?.sampleUserConsent?.setOnDetailConsentListener { needConsent, consentType ->
            viewBinding?.buttonAction?.showWithCondition(needConsent)
        }
    }

    private fun loadConsentComponent(collectionId: String, version: String) {
        val consentParam = ConsentCollectionParam(
            collectionId = collectionId,
            version = version
        )
        viewBinding?.sampleUserConsent?.load(consentParam)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.sampleUserConsent?.onDestroy()
    }

    companion object {
        private const val SCREEN_NAME = "User Consent Debug Fragment"
        private const val COLLECTION_ID_SAMPLE = "6d45e8ce-d46d-4f0e-bf0c-a93f82f75e36"
    }
}
