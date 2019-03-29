package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.logisticinputreceiptshipment.R;
import com.tokopedia.logisticinputreceiptshipment.view.data.CourierSelectionModel;
import com.tokopedia.transaction.common.data.order.CourierServiceModel;
import com.tokopedia.transaction.common.data.order.CourierViewModel;
import com.tokopedia.transaction.common.listener.ToolbarChangeListener;

import java.util.List;

/**
 * Created by kris on 1/5/18. Tokopedia
 */

public class ServiceSelectionFragment extends TkpdBaseV4Fragment
        implements OrderServiceAdapter.OrderServiceAdapterListener {

    private static final String COURIER_MODEL_EXTRA = "COURIER_MODEL_EXTRA";

    private ServiceSelectionListener listener;

    private ToolbarChangeListener toolbarListener;

    public static ServiceSelectionFragment createFragment(CourierViewModel model) {
        ServiceSelectionFragment fragment = new ServiceSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COURIER_MODEL_EXTRA, model);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ServiceSelectionListener) context;
        toolbarListener = (ToolbarChangeListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ServiceSelectionListener) activity;
        toolbarListener = (ToolbarChangeListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_selection_courier, container, false);
        CourierViewModel courierViewModel = getArguments()
                .getParcelable(COURIER_MODEL_EXTRA);
        List<CourierServiceModel> courierServiceModelList = courierViewModel.getCourierServiceList();
        RecyclerView recyclerView = view.findViewById(R.id.courier_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrderServiceAdapter(courierServiceModelList,
                courierViewModel.getCourierId(), courierViewModel.getCourierName(), this));
        toolbarListener.onChangeTitle(getString(R.string.label_select_service_logistic_module));
        return view;
    }

    @Override
    public void onServiceSelected(CourierSelectionModel model) {
        listener.onFinishSelectShipment(model);
    }

    public interface ServiceSelectionListener {

        void onFinishSelectShipment(CourierSelectionModel courierSelectionModel);

    }

}
