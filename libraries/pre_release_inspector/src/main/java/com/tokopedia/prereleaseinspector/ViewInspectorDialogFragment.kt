package com.tokopedia.prereleaseinspector

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.core.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_inspector_dialog.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

class ViewInspectorDialogFragment : DialogFragment(), DialogListener, CoroutineScope {

    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + masterJob

    val backgroundCoroutineContext: CoroutineContext
        get() = Dispatchers.Default + masterJob


    private var viewList: MutableList<View> = ArrayList()

    private var optionList: MutableList<View> = ArrayList()

    private var adapter: OptionPickerAdapter? = null

    fun setViewList(viewList: List<View>) {
        this.viewList.clear()
        this.viewList.addAll(viewList)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val size = Point()
            val display = dialog.window?.windowManager?.defaultDisplay
            display?.getSize(size)
            val width = size.x
            dialog.window?.setLayout((width * 0.75).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.CENTER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_inspector_dialog, container)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        initListener()
        setupRecyclerView()
        loadData()
    }

    override fun updateAllViewsWithId(id: Int, updater: (viewToUpdate: View) -> Unit) {
        launch {
            loadingView.show()
            runUpdateAllViews(id, updater)
            loadingView.hide()
            if (closeDialogCheckBox.isChecked) dismiss()
        }
    }

    private fun runUpdateAllViews(id: Int, updater: (viewToUpdate: View) -> Unit) {
        for (view in viewList) {
            if (view.id == id) {
                updater(view)
            }
        }
    }

    private fun initListener() {
        searchInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)) {
                    populateDataList(optionList)
                } else {
                    val filterResult = filterOptions(s.toString())
                    populateDataList(filterResult)
                    if (!filterResult.isEmpty()) {
                        hideEmpty()
                    } else {
                        showEmpty()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun populateDataList(dataList: List<View>) {
        adapter?.setData(dataList)
    }

    private fun filterOptions(filter: String): List<View> {
        val filteredList = ArrayList<View>()
        for (option in optionList) {

            if (getIdNameFromView(option).contains(filter, ignoreCase = true)
                    || getClassNameFromView(option).contains(filter, ignoreCase = true)
                    || getTextFromView(option).contains(filter, ignoreCase = true)) {
                filteredList.add(option)
            }
        }
        return filteredList
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dialogRecyclerView.layoutManager = layoutManager
        adapter = OptionPickerAdapter(this)
        dialogRecyclerView.adapter = adapter
    }

    private fun loadData() {
        launch {
            loadingView.show()
            removeViewWithNoIdAndText()
            sortViewListByViewType()
            optionList.clear()
            optionList.addAll(removeViewWithSameId(viewList))
            populateDataList(optionList)
            loadingView.hide()
            searchInput.visibility = View.VISIBLE
        }
    }

    private suspend fun removeViewWithNoIdAndText() = withContext(backgroundCoroutineContext) {
            viewList.removeAll { getIdNameFromView(it).isEmpty() && getTextFromView(it).isEmpty() }
    }

    private suspend fun removeViewWithSameId(list: MutableList<View>) : MutableList<View> = withContext(backgroundCoroutineContext) {
        list.distinctBy { it.id }.toMutableList()
    }

    private suspend fun sortViewListByViewType() = withContext(backgroundCoroutineContext)  {
        Collections.sort(viewList, object: Comparator<View> {
            override fun compare(o1: View, o2: View): Int {
                return compareValues(o1.javaClass.name, o2.javaClass.name)
            }
        })
    }

    private fun showEmpty() {
        emptyView.visibility = View.VISIBLE
    }

    private fun hideEmpty() {
        emptyView.visibility = View.GONE
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        masterJob.cancel()
    }
}

class OptionPickerAdapter(private val listener: DialogListener) : RecyclerView.Adapter<OptionPickerViewHolder>() {

    internal var optionList: MutableList<View> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionPickerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_inspector_view_item, parent, false)
        return OptionPickerViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: OptionPickerViewHolder, position: Int) {
        holder.bind(optionList[position], if (position > 0) optionList[position - 1] else null)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    fun setData(optionList: List<View>) {
        this.optionList.clear()
        if (optionList != null) {
            this.optionList.addAll(optionList)
        }
        notifyDataSetChanged()
    }
}

class OptionPickerViewHolder(private val rootView: View, private val listener: DialogListener) : RecyclerView.ViewHolder(rootView) {

    private val sectionHeader: TextView
    private val itemText: TextView
    private val propertyList: ViewGroup

    init {
        itemText = rootView.findViewById<View>(R.id.itemText) as TextView
        sectionHeader = rootView.findViewById<View>(R.id.sectionHeader) as TextView
        propertyList = rootView.findViewById(R.id.propertyList)
    }

    fun bind(item: View, prevItem: View?) {
        itemText.text = getViewDescription(item)

        propertyList.removeAllViews()
        
        rootView.setOnClickListener {
            if (propertyList.childCount == 0) {
                populatePropertyList(item)
            } else {
                propertyList.removeAllViews()
            }
        }

        adjustSectionHeader(item, prevItem)
    }

    private fun populatePropertyList(item: View) {

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_width),
                    text = convertPixelsToDp(item.width, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams {
                                width = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_height),
                    text = convertPixelsToDp(item.height, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams {
                                height = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_margin_left),
                    text = convertPixelsToDp(item.marginLeft, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                leftMargin = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_margin_top),
                    text = convertPixelsToDp(item.marginTop, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                topMargin = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_margin_right),
                    text = convertPixelsToDp(item.marginRight, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                rightMargin = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_margin_bottom),
                    text = convertPixelsToDp(item.marginBottom, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, {viewToUpdate ->
                            viewToUpdate.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                bottomMargin = convertDpToPixel(safeParse(it.getText()), propertyList.context)
                        }})
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_padding_left),
                    text = convertPixelsToDp(item.paddingLeft, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            viewToUpdate.setPadding(convertDpToPixel(safeParse(it.getText()), propertyList.context),
                                    item.paddingTop, item.paddingRight, item.paddingBottom)
                        })
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_padding_top),
                    text = convertPixelsToDp(item.paddingTop, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            viewToUpdate.setPadding(item.paddingLeft,
                                    convertDpToPixel(safeParse(it.getText()), propertyList.context), item.paddingRight, item.paddingBottom)
                        })
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_padding_right),
                    text = convertPixelsToDp(item.paddingRight, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            viewToUpdate.setPadding(item.paddingLeft, item.paddingTop,
                                    convertDpToPixel(safeParse(it.getText()), propertyList.context), item.paddingBottom)
                        })
                    })
            propertyList.addView(it)
        }
        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_padding_bottom),
                    text = convertPixelsToDp(item.paddingBottom, item.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            viewToUpdate.setPadding(item.paddingLeft, item.paddingTop, item.paddingRight,
                                    convertDpToPixel(safeParse(it.getText()), propertyList.context))
                        })
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_background_color),
                    text = String.format("#%06X", 0xFFFFFF and ((item.background as? ColorDrawable)?.color ?: 0xFFFFFF)),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            viewToUpdate.setBackgroundColor(Color.parseColor(it.getText()))
                        })
                    })
            propertyList.addView(it)
        }

        if (item !is TextView) {
            return
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_text),
                    text = item.text.toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            (viewToUpdate as? TextView)?.setText(it.getText())
                        })
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_text_color),
                    text = String.format("#%06X", 0xFFFFFF and item.currentTextColor),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            (viewToUpdate as? TextView)?.setTextColor(Color.parseColor(it.getText()))
                        })
                    })
            propertyList.addView(it)
        }

        ViewInspectorConfigItem(propertyList.context).let {
            it.setup(
                    hint = item.context.resources.getString(R.string.view_inspector_hint_config_text_size),
                    text = convertPixelsToSp(item.textSize, propertyList.context).toString(),
                    listener = View.OnClickListener { v ->
                        listener.updateAllViewsWithId(item.id, { viewToUpdate ->
                            it.getText().toFloatOrNull()?.let { size ->
                                (viewToUpdate as? TextView)?.setTextSize(size)
                            }
                        })
                    })
            propertyList.addView(it)
        }
    }

    private fun safeParse(number: String): Int {
        try {
            return Integer.parseInt(number)
        } catch (e: NumberFormatException) {
            return 1
        }

    }

    private fun convertDpToPixel(dp: Int, context: Context) : Int {
        val result = dp.toFloat() * context.resources.getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat()

        return result.roundToInt()
    }

    private fun convertPixelsToDp(px: Int, context: Context) : Int {
        val result = px.toFloat() / (context.getResources().getDisplayMetrics().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat())

        return result.roundToInt()
    }

    private fun convertPixelsToSp(px: Float, context: Context) : Int {
        val result = px / context.getResources().getDisplayMetrics().scaledDensity

        return result.roundToInt()
    }

    private fun adjustSectionHeader(item: View, prevItem: View?) {

        if (prevItem == null || item.javaClass.name != prevItem.javaClass.name) {
            showSectionHeader(item.javaClass.name)
        } else {
            hideSectionHeader()
        }
    }

    private fun hideSectionHeader() {
        sectionHeader.visibility = View.GONE
    }

    private fun showSectionHeader(sectionTitle: String?) {
        sectionHeader.visibility = View.VISIBLE
        sectionHeader.text = sectionTitle
    }
}

private fun getIdNameFromView(view: View) : String {
    try {
        return view.context?.resources?.getResourceEntryName(view.getId()) ?: ""
    } catch (e: Resources.NotFoundException) {
        return ""
    }
}

private fun getClassNameFromView(view: View) : String {
    return  view.javaClass.name
}

private fun getTextFromView(view: View) : String {
    return (view as? TextView)?.text?.toString() ?: ""
}

private fun getViewDescription(view: View) : String {
    val idName = getIdNameFromView(view)
    val contentText = getTextFromView(view)

    if (contentText.isEmpty()) {
        return idName
    } else if (idName.isEmpty()) {
        return "($contentText)"
    } else {
        return "$idName ($contentText)"
    }
}

interface DialogListener {
    fun updateAllViewsWithId(id: Int, updater: (viewToUpdate: View) -> Unit)
}
