package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.logisticorder.R;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.CourierSelectionModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierUiModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.ListCourierUiModel;

import static com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment.ConfirmShippingActivity.SELECT_SERVICE_FRAGMENT_TAG;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class CourierSelectionFragment extends TkpdBaseV4Fragment implements OrderCourierAdapter.OrderCourierAdapterListener {

    private static final String ORDER_COURIER_EXTRAS = "ORDER_COURIER_EXTRAS";

    private OrderCourierFragmentListener listener;

    private ToolbarChangeListener toolbarListener;

    public static CourierSelectionFragment createInstance(ListCourierUiModel model) {
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
    public void onCourierSelected(CourierUiModel courierUiModel) {
        if (courierUiModel.getCourierServiceList().size() > 1) {
            ServiceSelectionFragment serviceSelectionFragment = ServiceSelectionFragment
                    .createFragment(courierUiModel);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_left)
                    .add(R.id.main_view, serviceSelectionFragment, SELECT_SERVICE_FRAGMENT_TAG)
                    .commit();
        } else {
            toolbarListener.onChangeTitle(getString(R.string.title_confirm_shipment_logistic_module));
            CourierSelectionModel model = new CourierSelectionModel();
            model.setCourierName(courierUiModel.getCourierName());
            model.setCourierId(courierUiModel.getCourierId());
            model.setServiceId(courierUiModel.getCourierServiceList().get(0).getServiceId());
            model.setServiceName(courierUiModel.getCourierServiceList().get(0).getServiceName());
            listener.onCourierAdapterSelected(model);
        }
    }

    public interface OrderCourierFragmentListener {

        void onCourierAdapterSelected(CourierSelectionModel model);

    }
}
