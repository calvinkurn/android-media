package com.tokopedia.topads.common.view.widget

import android.content.Context
import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.listener.RadioCheckable
import java.util.concurrent.atomic.AtomicInteger

class TopAdsSubtitledRadioGroup: LinearLayout {
    private var checkedId = View.NO_ID
    private var protectFromCheckedChange = false
    private var onCheckedChangeListener: OnCheckedChangeListener? = null
    private lateinit var passThroughListener: PassThroughHierarchyChangeListener
    private lateinit var childOnCheckedChangeListener: RadioCheckable.OnCheckedChangeListener

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(attrs)
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes){
        init(attrs)
    }

    private fun init(attrs: AttributeSet){
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopAdsSubtitledRadioGroup)
        try {
            checkedId = styledAttributes.getResourceId(R.styleable.TopAdsSubtitledRadioGroup_checked_id, View.NO_ID)
        } finally {
            styledAttributes.recycle()
        }
        init()
    }

    private fun init(){
        childOnCheckedChangeListener = CheckedStateTracker()
        passThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(passThroughListener)
    }

    override fun addView(child: View?) {
        if (child is RadioCheckable){
            if (child.isChecked){
                protectFromCheckedChange = true
                if (checkedId != View.NO_ID){
                    setCheckedStateForView(checkedId, false)
                }
                protectFromCheckedChange = false
                setCheckedId(child.id, true)
            }
        }
        super.addView(child)
    }

    override fun setOnHierarchyChangeListener(listener: OnHierarchyChangeListener?) {
        passThroughListener.onHierarchyChangeListener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (checkedId != View.NO_ID){
            protectFromCheckedChange = true
            setCheckedStateForView(checkedId, true)
            protectFromCheckedChange = false
            setCheckedId(checkedId, true)
        }
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    fun clearCheck(){
        check(View.NO_ID)
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener
    }

    fun setOnCheckedChangeListener(onCheckedChangeListener: (radioGroup: View, isChecked: Boolean, checkedId: Int) -> Unit){
        this.onCheckedChangeListener = object: OnCheckedChangeListener{
            override fun onCheckedChanged(radioGroup: View, isChecked: Boolean, checkedId: Int) {
                onCheckedChangeListener(radioGroup, isChecked, checkedId)
            }

        }
    }

    fun getOnCheckedChangeListener(): OnCheckedChangeListener?{
        return onCheckedChangeListener
    }

    private fun check(@IdRes id: Int){
        if (id != View.NO_ID && id == checkedId){
            return
        }

        if (checkedId != View.NO_ID){
            setCheckedStateForView(checkedId, false)
        }
        if (id != View.NO_ID){
            setCheckedStateForView(id, true)
        }
        setCheckedId(id, true)
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView: View? = findViewById(viewId)
        checkedView?.let {
            if (checkedView is RadioCheckable){
                checkedView.isChecked = checked
            }
        }
    }

    private fun setCheckedId(@IdRes id: Int, checked: Boolean){
        checkedId = id
        onCheckedChangeListener?.onCheckedChanged(this, checked, id)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(radioGroup: View, isChecked: Boolean, checkedId: Int)
    }

    inner class PassThroughHierarchyChangeListener: OnHierarchyChangeListener {
        var onHierarchyChangeListener: OnHierarchyChangeListener? = null

        override fun onChildViewRemoved(parent: View?, child: View?) {
            if (parent == this@TopAdsSubtitledRadioGroup && child is RadioCheckable){
                child.removeOnCheckChangeListener(childOnCheckedChangeListener)
            }
            onHierarchyChangeListener?.run {
                onChildViewRemoved(parent, child)
            }
        }

        override fun onChildViewAdded(parent: View?, child: View?) {
            if (parent is TopAdsSubtitledRadioGroup && child is RadioCheckable){
                var id = child.id
                if (id == View.NO_ID){
                    id = ViewUtils.generateViewId()
                    child.id = id
                }

                child.addOnCheckChangeListener(childOnCheckedChangeListener)
            }
            onHierarchyChangeListener?.run {
                onChildViewAdded(parent, child)
            }
        }
    }

    object ViewUtils {
        const val MAX_ID_VALUE = 0x00FFFFFF

        private val sNextGeneratedId = AtomicInteger(1)

        fun generateViewId(): Int {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                while (true) {
                    val result = sNextGeneratedId.get()
                    var newValue = result + 1
                    if (newValue > MAX_ID_VALUE) newValue = 1
                    if (sNextGeneratedId.compareAndSet(result, newValue)) {
                        return result
                    }
                }
            } else {
                return View.generateViewId()
            }
        }

    }

    inner class CheckedStateTracker: RadioCheckable.OnCheckedChangeListener{
        override fun onCheckedChanged(radioGroup: View, isChecked: Boolean) {
            if (protectFromCheckedChange){
                return
            }
            protectFromCheckedChange = true
            if (checkedId != View.NO_ID){
                setCheckedStateForView(checkedId, false)
            }
            protectFromCheckedChange = false

            val id = radioGroup.id
            setCheckedId(id, isChecked)
        }

    }
}