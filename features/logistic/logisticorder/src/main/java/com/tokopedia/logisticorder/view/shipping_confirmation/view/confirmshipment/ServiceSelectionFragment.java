package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.logisticorder.R;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.CourierSelectionModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierServiceModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierUiModel;

import java.util.List;

/**
 * Created by kris on 1/5/18. Tokopedia
 */

public class ServiceSelectionFragment extends TkpdBaseV4Fragment
        implements OrderServiceAdapter.OrderServiceAdapterListener {

    private static final String COURIER_MODEL_EXTRA = "COURIER_MODEL_EXTRA";

    private ServiceSelectionListener listener;

    private ToolbarChangeListener toolbarListener;

    public static ServiceSelectionFragment createFragment(CourierUiModel model) {
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
        CourierUiModel courierUiModel = getArguments()
                .getParcelable(COURIER_MODEL_EXTRA);
        List<CourierServiceModel> courierServiceModelList = courierUiModel.getCourierServiceList();
        RecyclerView recyclerView = view.findViewById(R.id.courier_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrderServiceAdapter(courierServiceModelList,
                courierUiModel.getCourierId(), courierUiModel.getCourierName(), this));
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
