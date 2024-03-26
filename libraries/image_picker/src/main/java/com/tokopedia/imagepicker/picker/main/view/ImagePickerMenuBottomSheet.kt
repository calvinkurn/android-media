package com.tokopedia.imagepicker.picker.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.imagepicker.R
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify


class ImagePickerMenuBottomSheet : BottomSheetUnify() {

    companion object {
        private const val LIST_MENU = "productRule"
        @JvmStatic
        fun newInstance(listMenu: List<String>): ImagePickerMenuBottomSheet {
            return ImagePickerMenuBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(LIST_MENU, ArrayList(listMenu))
                }
            }
        }
    }

    private var listMenu: List<String>? = listOf()
    private var imagePickerListUnify: ListUnify? = null
    private var childView: View? = null
    private var itemClickListener: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        childView = inflater.inflate(R.layout.image_picker_bottomsheet_menu, container, false)
        showHeader = false
        showCloseIcon = false
        setChild(childView)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupContent()
    }

    private fun setupContent() {
        val listItemUnify = listMenu?.map {
            val data = ListItemUnify(it,"")
            data.setVariant()
            data
        }.orEmpty()
        imagePickerListUnify?.setData(ArrayList(listItemUnify))
        imagePickerListUnify?.setOnItemClickListener { _, _, pos, _ ->
            itemClickListener?.invoke()
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun getArgumentsData() {
        arguments?.run {
            listMenu = getStringArrayList(LIST_MENU)
        }
    }

    private fun setupView() {
        imagePickerListUnify = childView?.findViewById(R.id.image_picker_list_unify)
    }

    fun setOnItemClickListener(listener: () -> Unit) {
        this.itemClickListener = listener
    }
}
