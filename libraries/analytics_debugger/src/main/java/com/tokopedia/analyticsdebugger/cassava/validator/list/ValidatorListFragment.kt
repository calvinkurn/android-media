package com.tokopedia.analyticsdebugger.cassava.validator.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.di.CassavaComponentInstance
import com.tokopedia.analyticsdebugger.cassava.validator.main.ValidatorViewModel
import timber.log.Timber
import javax.inject.Inject

class ValidatorListFragment : Fragment() {

    private var listener: Listener? = null

    private lateinit var listingAdapter: FileListingAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: ValidatorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
                .get(ValidatorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_validator_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        listingAdapter = FileListingAdapter().also { adapter ->
            adapter.setOnItemClickListener {
                listener?.goToTestPage(it.first,
                        view.findViewById<ToggleButton>(R.id.toggle_cassava_source).isChecked)
            }
        }

        view.findViewById<ImageView>(R.id.iv_delete).setOnClickListener {
            viewModel.delete()
        }

        with(view.findViewById<RecyclerView>(R.id.rv)) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = listingAdapter
        }

        with(view.findViewById<ToggleButton>(R.id.toggle_cassava_source)) {
            viewModel.changeSource(isChecked)
            setOnCheckedChangeListener { _, isChecked ->
                viewModel.changeSource(isChecked)
            }
        }

        val searchBarTextField = view.findViewById<EditText>(R.id.searchbar_textfield)
        val searchBarClearButton = view.findViewById<ImageButton>(R.id.searchbar_clear)
        with(searchBarTextField) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearSearchBarFocus(this)
                    true
                } else false
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.run {
                        searchBarClearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                        // Filter test cases based on search query
                        val filteredListTests = viewModel.getListFiles().filter { it.second.contains(s.toString(), true) }
                        listingAdapter.setItems(filteredListTests)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /* do nothing */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* do nothing */
                }

            })
        }
        searchBarClearButton.setOnClickListener {
            searchBarTextField.text.clear()
            searchBarClearButton.visibility = View.GONE
            clearSearchBarFocus(searchBarTextField)
            listingAdapter.setItems(viewModel.getListFiles())
        }
    }

    private fun observeLiveData() {
        viewModel.listFiles.observe(viewLifecycleOwner, {
            Timber.d("List files: %s", it)
            listingAdapter.setItems(it)
        })
    }

    private fun clearSearchBarFocus(editText: EditText) {
        editText.clearFocus()
        val input = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        input?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun initInjector() {
        activity?.let {
            CassavaComponentInstance.getInstance(it).inject(this)
        }
    }

    interface Listener {
        fun goToTestPage(filepath: String, isFromNetwork: Boolean)
    }

    companion object {

        const val TRACKER_ROOT_PATH = "tracker"
        fun newInstance(): ValidatorListFragment = ValidatorListFragment()
    }
}