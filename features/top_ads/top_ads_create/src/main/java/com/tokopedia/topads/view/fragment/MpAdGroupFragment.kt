package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.create.databinding.MpAdGroupFragmentBinding

class MpAdGroupFragment : BaseDaggerFragment() {

    companion object{
        fun newInstance() : MpAdGroupFragment{
            return MpAdGroupFragment()
        }
    }

    private var binding:MpAdGroupFragmentBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MpAdGroupFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
    }

    private fun setupHeader(){
      binding?.adGroupHeader?.apply {
          setNavigationOnClickListener {
          activity?.onBackPressed()
          }
      }
    }

    override fun getScreenName() = ""
    override fun initInjector() {}

}
