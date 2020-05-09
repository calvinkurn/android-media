package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View

/**
 * Created By @ilhamsuaib on 09/05/20
 */

class DuplicateVoucherFragment : BaseDetailFragment() {

    companion object {
        fun newInstance(): DuplicateVoucherFragment {
            return DuplicateVoucherFragment()
        }
    }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initInjector() {

    }

    override fun loadData(page: Int) {

    }

    override fun onFooterButtonClickListener() {

    }

    private fun setupView() {

    }
}