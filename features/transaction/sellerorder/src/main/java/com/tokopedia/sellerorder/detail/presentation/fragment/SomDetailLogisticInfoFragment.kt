package com.tokopedia.sellerorder.detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.FragmentSomDetailLogisticInfoBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLogisticInfoAdapter
import com.tokopedia.sellerorder.detail.presentation.model.LogisticInfoAllWrapper
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SomDetailLogisticInfoFragment : BaseDaggerFragment() {

    private var logisticInfoAllList: LogisticInfoAllWrapper? = null

    companion object {
        @JvmStatic
        fun newInstance(): SomDetailLogisticInfoFragment {
            return SomDetailLogisticInfoFragment()
        }

        const val KEY_SOM_LOGISTIC_INFO_ALL = "key_som_logistic_info_all"
        const val KEY_ID_CACHE_MANAGER_INFO_ALL = "key_id_cache_manager_info_all"
    }

    private var cacheManager: SaveInstanceCacheManager? = null
    private var cacheObjectId = ""

    private var binding by autoClearedNullable<FragmentSomDetailLogisticInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
            it.intent?.run {
                cacheObjectId = getStringExtra(KEY_ID_CACHE_MANAGER_INFO_ALL) ?: ""
            }
        }
        val manager = if (savedInstanceState == null) {
            SaveInstanceCacheManager(requireContext(), cacheObjectId)
        } else {
            cacheManager
        }
        logisticInfoAllList = manager?.get(KEY_SOM_LOGISTIC_INFO_ALL, LogisticInfoAllWrapper::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSomDetailLogisticInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        initToolbar()
        initView()
    }

    private fun initView() {
        binding?.rvLogisticInfo?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = logisticInfoAllList?.let { SomDetailLogisticInfoAdapter(it.logisticInfoAllList) }
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(binding?.logisticInfoToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                binding?.logisticInfoToolbar?.title = getString(R.string.title_logistic_info)
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

}