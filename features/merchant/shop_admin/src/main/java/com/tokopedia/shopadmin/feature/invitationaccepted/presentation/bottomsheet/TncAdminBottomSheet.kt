package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.shopadmin.databinding.BottomsheetTncAdminBinding
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.adapter.ItemTncAdminAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TncAdminBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetTncAdminBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetTncAdminBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    private fun setupViews() = binding?.run {
        setupTncAdapter()
    }

    private fun BottomsheetTncAdminBinding.setupTncAdapter() {
        rvTncList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemTncAdminAdapter(listOf())
        }
    }

    companion object {
        val TAG: String = TncAdminBottomSheet::class.java.simpleName
    }
}