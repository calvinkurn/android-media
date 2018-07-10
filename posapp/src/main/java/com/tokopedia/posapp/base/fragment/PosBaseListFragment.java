package com.tokopedia.posapp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.posapp.R;

/**
 * @author okasurya on 3/13/18.
 */

public abstract class PosBaseListFragment<T extends Visitable, F extends AdapterTypeFactory> extends BaseListFragment<T, F> {

}
