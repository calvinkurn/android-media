package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.utils.FilterListHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pranaymohapatra on 16/01/18.
 */

public class FilterFragment extends Fragment {

    @BindView(R2.id.main_content)
    View mainContent;
    @BindView(R2.id.filter_content)
    LinearLayout filterContent;
    @BindView(R2.id.button_textview)
    TextView buttonTextView;

    Unbinder unbinder;

    private static final String ARG_PARAM1 = "typecount";

    private int mTicketTypeCount;
    private List<FilterDomainModel> mFilters;

    EventSearchPresenter mPresenter;


    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance(int typecount) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typecount);
        fragment.setArguments(args);
        return fragment;
    }

    public void setData(List<FilterDomainModel> filters, EventSearchPresenter presenter) {
        this.mPresenter = presenter;
        this.mFilters = filters;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTicketTypeCount = getArguments().getInt(ARG_PARAM1);
        }

//        executeInjector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View maincontent = inflater.inflate(R.layout.filter_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, maincontent);
        buttonTextView.setText("Simpan");
        for (FilterDomainModel model : mFilters) {
            View filterListLayout = inflater.inflate(R.layout.filter_list_layout,
                    null);
            FilterListHolder holder = new FilterListHolder();
            ButterKnife.bind(holder,filterListLayout);
            holder.setRecyclerView(model,mPresenter);
            holder.setTvFilterLabel(model.getLabel());
            filterContent.addView(filterListLayout);
        }

        return maincontent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
