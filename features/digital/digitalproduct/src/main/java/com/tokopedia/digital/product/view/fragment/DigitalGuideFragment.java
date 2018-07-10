package com.tokopedia.digital.product.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.adapter.GuideAdapter;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;

import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class DigitalGuideFragment extends Fragment {

    private RecyclerView rvGuide;

    private DigitalGuideConnector connector;
    private GuideAdapter adapter;

    public static DigitalGuideFragment createInstance() {
        return new DigitalGuideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_guide, container, false);
        rvGuide = view.findViewById(R.id.rv_panduan);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        rvGuide.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        adapter = new GuideAdapter(getActivity());
        rvGuide.setAdapter(adapter);
        renderData();
    }

    private void renderData() {
        adapter.addData(connector.getGuideDataList());
    }

    public void setConnector(DigitalGuideConnector connector) {
        this.connector = connector;
    }

    public interface DigitalGuideConnector {

        List<GuideData> getGuideDataList();

    }
}
