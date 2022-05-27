package com.tokopedia.data_explorer.db_explorer.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.presentation.databases.DbExplorerActivity
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_data_explorer_layout.*

class DataExplorerFragment : BaseDaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data_explorer_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        databaseCard.setOnClickListener {
            val intent = Intent(context, DbExplorerActivity::class.java)
            startActivity(intent)
        }
        sharePrefCard.setOnClickListener {
            Toaster.build(sharePrefCard, "Not just winter this is coming soon too :D", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            // @Todo Add your Activity here
            //val intent = Intent(context, SharedPref::class.java)
            //startActivity(intent)
        }
    }

    override fun getScreenName() = ""
    override fun initInjector() {}

    companion object {
        fun newInstance() = DataExplorerFragment()
    }
}