package com.tokopedia.data_explorer.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.di.DbInspectorComponent
import com.tokopedia.data_explorer.presentation.Constants
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.fragment_database_list_layout.*
import javax.inject.Inject

class ContentFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val viewModel: ContentViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(ContentViewModel::class.java)
    }

    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE){
        arguments?.getString(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(Constants.Keys.DATABASE_PATH).orEmpty()
    }

    private val schemaName: String by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(Constants.Keys.SCHEMA_NAME).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_database_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_input_view.gone()
        viewModel.getTableInfo(databasePath, schemaName)
        observeViewModels()
        //rvDatabaseList.adapter = schemaAdapter
        //rvDatabaseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModels() {

    }


    override fun getScreenName() = ""
    override fun initInjector() = getComponent(DbInspectorComponent::class.java).inject(this)

    companion object {
        fun newInstance(schemaName: String, databaseName: String, databasePath: String) : ContentFragment {
            val fragment = ContentFragment()
            val bundle = Bundle().apply {
                putString(Constants.Keys.DATABASE_NAME, databaseName)
                putString(Constants.Keys.DATABASE_PATH, databasePath)
                putString(Constants.Keys.SCHEMA_NAME, schemaName)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}