package com.tokopedia.digital.product.view.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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

    private GuideAdapter adapter;

    private List<GuideData> guideDataList;

    public static DigitalGuideFragment createInstance(DigitalGuideConnector digitalGuideConnector) {
        DigitalGuideFragment fragment = new DigitalGuideFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_GUIDE_DATA, (ArrayList<? extends Parcelable>) digitalGuideConnector.getGuideDataList());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            guideDataList = savedInstanceState.getParcelableArrayList(EXTRA_GUIDE_DATA);
        } else if (getArguments() != null) {
            guideDataList = getArguments().getParcelableArrayList(EXTRA_GUIDE_DATA);
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
        if (guideDataList != null) {
            adapter.addData(guideDataList);
        }
    }

    public interface DigitalGuideConnector {

        List<GuideData> getGuideDataList();

    }
}
