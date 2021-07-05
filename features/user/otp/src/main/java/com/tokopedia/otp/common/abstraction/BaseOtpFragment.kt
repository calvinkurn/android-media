package com.tokopedia.otp.common.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */
abstract class BaseOtpFragment : BaseDaggerFragment() {

    abstract val viewBound: BaseOtpViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return viewBound.inflate(inflater, container)
    }
}