package com.tokopedia.usercomponents.userconsent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.databinding.FragmentDebugUserConsentBinding
import com.tokopedia.usercomponents.userconsent.common.UserConsentPayload
import com.tokopedia.usercomponents.userconsent.domain.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.UserConsentActionClickListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

class UserConsentDebugFragment: BaseDaggerFragment() {

    var viewBinding by autoClearedNullable<FragmentDebugUserConsentBinding>()

    override fun getScreenName(): String {
        return "User Consent Debug Fragment"
    }

    override fun initInjector() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentDebugUserConsentBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.textCollextionId?.editText?.setText("6d45e8ce-d46d-4f0e-bf0c-a93f82f75e36")
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
        val consentParam = ConsentCollectionParam(collectionId,version)
        viewBinding?.sampleUserConsent?.apply {
            actionText = viewBinding?.textActionButton?.editText?.text.toString()
        }?.load(viewLifecycleOwner, consentParam, object : UserConsentActionClickListener {
            override fun onCheckedChange(isChecked: Boolean) {

            }

            @SuppressLint("SetTextI18n")
            override fun onActionClicked(payload: UserConsentPayload) {
                viewBinding?.textPayloadData?.apply {
                    text = "PAYLOAD: \n\n$payload"
                }?.show()
            }

            override fun onFailed(throwable: Throwable) {
                Toast.makeText(context, throwable.message.orEmpty(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.sampleUserConsent?.onDestroy()
    }
}