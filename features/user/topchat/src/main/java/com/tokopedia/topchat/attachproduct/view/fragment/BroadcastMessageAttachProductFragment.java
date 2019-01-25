package com.tokopedia.topchat.attachproduct.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapter;
import com.tokopedia.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.attachproduct.view.activity.BroadcastMessageAttachProductActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BroadcastMessageAttachProductFragment extends AttachProductFragment {
    private final static int MAX_CHECKED = 3;
    private Button chooseButton;
    private HashSet<Integer> productIds = new HashSet<>();
    private ArrayList<AttachProductItemViewModel> checkedList = new ArrayList<>();

    public static BroadcastMessageAttachProductFragment newInstance(AttachProductContract.Activity checkedUIView) {
        Bundle args = new Bundle();
        BroadcastMessageAttachProductFragment fragment = new BroadcastMessageAttachProductFragment();
        fragment.setActivityContract(checkedUIView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        try {
            if (activity != null && activity.getIntent() != null){
                Intent intent = activity.getIntent();
                String shopName = intent.getStringExtra(BroadcastMessageAttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY);
                List<Integer> ids = intent.getIntegerArrayListExtra(BroadcastMessageAttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_IDS_KEY);
                List<HashMap<String, String>> products =
                        (List<HashMap<String, String>>) intent.getSerializableExtra(BroadcastMessageAttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_HASH_KEY);

                productIds.addAll(ids);
                updateButtonBasedOnChecked(productIds.size());
                for (HashMap<String, String> product: products){
                    checkedList.add(new AttachProductItemViewModel(
                            product.get(BroadcastMessageAttachProductActivity.PARAM_PRODUCT_URL),
                            product.get(BroadcastMessageAttachProductActivity.PARAM_PRODUCT_NAME),
                            Integer.parseInt(product.get(BroadcastMessageAttachProductActivity.PARAM_PRODUCT_ID)),
                            null,
                            product.get(BroadcastMessageAttachProductActivity.PARAM_PRODUCT_THUMBNAIL),
                            product.get(BroadcastMessageAttachProductActivity.PARAM_PRODUCT_PRICE),
                            shopName));
                }
            }
        } catch (Throwable t){}
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chooseButton = view.findViewById(R.id.send_button_attach_product);
        updateButtonBasedOnChecked(productIds.size());
    }

    @NonNull
    @Override
    protected BaseListAdapter<AttachProductItemViewModel, AttachProductListAdapterTypeFactory> createAdapterInstance() {
        adapter = new AttachProductListAdapter(getAdapterTypeFactory(), productIds, checkedList);
        return adapter;
    }

    @Override
    public void updateButtonBasedOnChecked(int checkedCount) {
        super.updateButtonBasedOnChecked(checkedCount);
        if (chooseButton != null)
            chooseButton.setText(getString(R.string.string_attach_product_choose_button_text,String.valueOf(checkedCount),
                String.valueOf(MAX_CHECKED)));
    }
}
