package com.tokopedia.shop_nib.presentation.submission_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.shop_nib.databinding.SsnActivityNibSubmissionSuccessBinding
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class NibSubmissionSuccessFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NibSubmissionSuccessFragment {
            return NibSubmissionSuccessFragment()
        }
    }

    private var binding by autoClearedNullable<SsnActivityNibSubmissionSuccessBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsnActivityNibSubmissionSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }


}
