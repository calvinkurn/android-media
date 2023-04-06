package com.tokopedia.developer_options.sharedpref

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import java.io.File

class SharedPreferenceListFragment :
    BaseSearchListFragment<Visitable<SimpleTypeFactory>, SimpleTypeFactory>() {

    private var buttonSearch: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shared_pref_list, container, false)
        buttonSearch = view.findViewById(R.id.button_search)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSearch!!.setOnClickListener { v ->
            search(searchInputView.searchBarTextField.text.toString())
        }
        searchInputView.searchBarPlaceholder = "Cari"
    }

    override fun getScreenName(): String = ""

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    fun search(searchName: String?) {
        val sharedPrefFolder =
            File("/data/data/" + GlobalConfig.getPackageApplicationName() + "/shared_prefs")
        val folderList = sharedPrefFolder.list()?.asSequence()?.filter { it ->
            if (searchName.isNullOrEmpty()) {
                true
            } else {
                it.contains(searchName)
            }
        }?.map { it.replace(".xml", "") }
            ?.map { IdViewModel(it) }?.toList()
            ?: emptyList()
        adapter.setElements(folderList)
    }

    override fun loadData(page: Int) {
        search("")
    }

    override fun getAdapterTypeFactory(): SimpleTypeFactory {
        return SimpleTypeFactory()
    }

    override fun onItemClicked(visitable: Visitable<SimpleTypeFactory>) {
        if (visitable is IdViewModel) {
            val intent = Intent(
                context,
                SharedPrefDetailFragmentActivity::class.java
            )
            intent.putExtra(DeveloperOptionActivity.SHARED_PREF_FILE, visitable.id)
            startActivity(intent)
        }
    }

    override fun initInjector() {
        //noop
    }

    override fun onSearchSubmitted(text: String) {
        search(text)
    }

    override fun onSearchTextChanged(text: String) {
        search(text)
    }

    companion object {

        @JvmStatic
        val TAG = SharedPreferenceListFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return SharedPreferenceListFragment()
        }
    }
}
