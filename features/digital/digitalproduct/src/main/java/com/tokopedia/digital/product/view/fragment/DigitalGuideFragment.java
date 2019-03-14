package com.tokopedia.digital.product.view.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.adapter.GuideAdapter;
import com.tokopedia.digital.product.view.model.GuideData;
import com.tokopedia.digital.utils.LinearLayoutManagerNonScroll;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 07/06/18.
 */

public class DigitalGuideFragment extends Fragment {

    private static final String EXTRA_GUIDE_DATA = "EXTRA_GUIDE_DATA";

    private RecyclerView rvGuide;

    private DigitalGuideConnector connector;
    private GuideAdapter adapter;

    private List<GuideData> guideDataList;

    public static DigitalGuideFragment createInstance() {
        return new DigitalGuideFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            guideDataList = savedInstanceState.getParcelableArrayList(EXTRA_GUIDE_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_guide, container, false);
        rvGuide = view.findViewById(R.id.rv_guide);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(EXTRA_GUIDE_DATA, (ArrayList<? extends Parcelable>) guideDataList);
    }

    private void initView() {
        rvGuide.setLayoutManager(new LinearLayoutManagerNonScroll(getActivity()));
        adapter = new GuideAdapter(getActivity());
        rvGuide.setAdapter(adapter);
        renderData();
    }

    private void renderData() {
        if (guideDataList == null) {
            guideDataList = connector.getGuideDataList();
        }

        adapter.addData(guideDataList);
    }

    public void setConnector(DigitalGuideConnector connector) {
        this.connector = connector;
    }

    public interface DigitalGuideConnector {

        List<GuideData> getGuideDataList();

    }
}
