package com.tokopedia.privacyaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.privacyaccount.databinding.FragmentMainPrivacyAccountBinding

class PrivacyAccountFragment : Fragment() {

    private var viewBinding: FragmentMainPrivacyAccountBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainPrivacyAccountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    companion object {
        fun newInstance() = PrivacyAccountFragment()
    }
}
