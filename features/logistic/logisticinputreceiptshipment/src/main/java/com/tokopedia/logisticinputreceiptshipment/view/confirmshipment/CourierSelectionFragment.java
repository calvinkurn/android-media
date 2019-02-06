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
import com.tokopedia.transaction.common.data.order.CourierViewModel;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;
import com.tokopedia.transaction.common.listener.ToolbarChangeListener;

import static com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.ConfirmShippingActivity.SELECT_SERVICE_FRAGMENT_TAG;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class CourierSelectionFragment extends TkpdBaseV4Fragment implements OrderCourierAdapter.OrderCourierAdapterListener {

    private static final String ORDER_COURIER_EXTRAS = "ORDER_COURIER_EXTRAS";

    private OrderCourierFragmentListener listener;

    private ToolbarChangeListener toolbarListener;

    public static CourierSelectionFragment createInstance(ListCourierViewModel model) {
        CourierSelectionFragment fragment = new CourierSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_COURIER_EXTRAS, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courier_selection, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.courier_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrderCourierAdapter(getArguments()
                .getParcelable(ORDER_COURIER_EXTRAS), this));
        toolbarListener.onChangeTitle(getString(R.string.label_select_courier_logistic_module));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OrderCourierFragmentListener) context;
        toolbarListener = (ToolbarChangeListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OrderCourierFragmentListener) activity;
        toolbarListener = (ToolbarChangeListener) activity;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCourierSelected(CourierViewModel courierViewModel) {
        if (courierViewModel.getCourierServiceList().size() > 1) {
            ServiceSelectionFragment serviceSelectionFragment = ServiceSelectionFragment
                    .createFragment(courierViewModel);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_left)
                    .add(R.id.main_view, serviceSelectionFragment, SELECT_SERVICE_FRAGMENT_TAG)
                    .commit();
        } else {
            toolbarListener.onChangeTitle(getString(R.string.title_confirm_shipment_logistic_module));
            CourierSelectionModel model = new CourierSelectionModel();
            model.setCourierName(courierViewModel.getCourierName());
            model.setCourierId(courierViewModel.getCourierId());
            model.setServiceId(courierViewModel.getCourierServiceList().get(0).getServiceId());
            model.setServiceName(courierViewModel.getCourierServiceList().get(0).getServiceName());
            listener.onCourierAdapterSelected(model);
        }
    }

    public interface OrderCourierFragmentListener {

        void onCourierAdapterSelected(CourierSelectionModel model);

    }
}
