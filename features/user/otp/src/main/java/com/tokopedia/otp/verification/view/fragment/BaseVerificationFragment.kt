package com.tokopedia.otp.verification.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.viewbinding.BaseVerificationViewBinding

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */
abstract class BaseVerificationFragment : BaseDaggerFragment() {

    abstract val viewBound: BaseVerificationViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return viewBound.inflate(inflater, container)
    }
}