package com.tokopedia.abstraction.base.view.activity;

/**
 * If the module that extends the class is Dynamic Feature module,
 * it is recommended to extend this class to make sure the resource R is correct
 *
 * to override use the full package name: for example
 * override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple
 * override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view
 */
public abstract class BaseSimpleDFActivity extends BaseSimpleActivity {

    @Override
    protected abstract int getLayoutRes();

    protected abstract int getParentViewResourceID();

}