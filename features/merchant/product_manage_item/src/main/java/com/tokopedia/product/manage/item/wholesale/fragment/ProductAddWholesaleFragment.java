package com.tokopedia.product.manage.item.wholesale.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel;
import com.tokopedia.product.manage.item.utils.ProductEditCurrencyType;
import com.tokopedia.product.manage.item.wholesale.adapter.WholesaleAddAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleFragment extends BaseDaggerFragment implements WholesaleAddAdapter.Listener {

    public static final String EXTRA_PRODUCT_WHOLESALE = "EXTRA_PRODUCT_WHOLESALE";
    public static final String EXTRA_PRODUCT_MAIN_PRICE = "EXTRA_PRODUCT_MAIN_PRICE";
    public static final String SAVE_PRODUCT_WHOLESALE = "SAVE_PRODUCT_WHOLESALE";

    private static final int MAX_WHOLESALE = 5;
    private static final int DEFAULT_QTY_WHOLESALE = 2;
    private static final int DEFAULT_ADD_QTY = 1;
    private static final int DEFAULT_LESS_PRICE_RP = 1;
    private static final double DEFAULT_LESS_PRICE_USD = 0.01;

    private WholesaleAddAdapter wholesaleAdapter;
    private TextView addWholesaleTextView;
    private TextView mainPriceTextView;
    private TextView wholesaleVariantInfoTextView;
    private Button buttonSave;
    private ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList;
    private ArrayList<ProductWholesaleViewModel> productWholesaleViewModelListTemp;
    private double productPrice;
    private boolean officialStore;
    private boolean hasVariant;
    @ProductEditCurrencyType
    private int currencyType;

    public static ProductAddWholesaleFragment newInstance() {
        Bundle args = new Bundle();
        ProductAddWholesaleFragment fragment = new ProductAddWholesaleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Intent activityIntent = getActivity().getIntent();

        productWholesaleViewModelList = activityIntent.getParcelableArrayListExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_WHOLESALE_LIST);
        productWholesaleViewModelListTemp = activityIntent.getParcelableArrayListExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_WHOLESALE_LIST);
        productPrice = activityIntent.getDoubleExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_MAIN_PRICE, 0);
        officialStore = activityIntent.getBooleanExtra(ProductAddWholesaleActivity.EXTRA_OFFICIAL_STORE, false);
        hasVariant = activityIntent.getBooleanExtra(ProductAddWholesaleActivity.EXTRA_HAS_VARIANT, false);
        initCurrency(activityIntent.getIntExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_CURRENCY, 1));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_add_wholesale, container, false);

        RecyclerView recyclerViewWholesale = root.findViewById(R.id.recycler_view_wholesale);
        recyclerViewWholesale.setLayoutManager(new LinearLayoutManager(recyclerViewWholesale.getContext(), LinearLayoutManager.VERTICAL, false));
        wholesaleAdapter = new WholesaleAddAdapter(productPrice, officialStore);
        wholesaleAdapter.setListener(this);
        wholesaleAdapter.setHasStableIds(true);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);
        recyclerViewWholesale.setNestedScrollingEnabled(false);

        buttonSave = root.findViewById(R.id.button_save);
        mainPriceTextView = root.findViewById(R.id.text_main_price);
        wholesaleVariantInfoTextView = root.findViewById(R.id.text_view_wholesale_variant_info);
        if (hasVariant) {
            wholesaleVariantInfoTextView.setText(getActivity().getText(R.string.product_add_wholesale_notice_variant));
            wholesaleVariantInfoTextView.setVisibility(View.VISIBLE);
        }
        addWholesaleTextView = root.findViewById(R.id.text_view_add_wholesale);
        addWholesaleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WholesaleModel lastItem = wholesaleAdapter.getLastItem();
                WholesaleModel newWholesale;
                switch (currencyType) {
                    case ProductEditCurrencyType.USD:
                        newWholesale = new WholesaleModel(DEFAULT_QTY_WHOLESALE, productPrice - DEFAULT_LESS_PRICE_USD);
                        if (lastItem != null)
                            newWholesale = new WholesaleModel(lastItem.getQtyMin() + DEFAULT_ADD_QTY, lastItem.getQtyPrice() - DEFAULT_LESS_PRICE_USD);
                        break;
                    default:
                    case ProductEditCurrencyType.RUPIAH:
                        newWholesale = new WholesaleModel(DEFAULT_QTY_WHOLESALE, productPrice - DEFAULT_LESS_PRICE_RP);
                        if (lastItem != null)
                            newWholesale = new WholesaleModel(lastItem.getQtyMin() + DEFAULT_ADD_QTY, lastItem.getQtyPrice() - DEFAULT_LESS_PRICE_RP);
                        break;

                }

                wholesaleAdapter.addItem(newWholesale);
                updateWholesaleButton();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasVariant) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                            .setTitle(getString(R.string.dialog_add_wholesale_title))
                            .setMessage(getString(R.string.dialog_add_wholesale_message))
                            .setPositiveButton(getString(R.string.label_add), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    exitWholesaleActivity();
                                }
                            }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                } else {
                    exitWholesaleActivity();
                }
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVE_PRODUCT_WHOLESALE)) {
                productWholesaleViewModelList = savedInstanceState.getParcelableArrayList(SAVE_PRODUCT_WHOLESALE);
            }
        }

        renderData(productWholesaleViewModelList, productPrice);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        productWholesaleViewModelList = wholesaleAdapter.getProductWholesaleViewModels();
        outState.putParcelableArrayList(SAVE_PRODUCT_WHOLESALE, productWholesaleViewModelList);
    }

    public void exitWholesaleActivity() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_PRODUCT_WHOLESALE, wholesaleAdapter.getProductWholesaleViewModels());
        intent.putExtra(EXTRA_PRODUCT_MAIN_PRICE, productPrice);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void renderData(ArrayList<ProductWholesaleViewModel> productWholesaleViewModelArrayList, double productPrice) {
        String currencyString = CurrencyFormatUtil.convertPriceValue(productPrice, true);
        setWholesalePrice(productWholesaleViewModelArrayList);
        switch (currencyType) {
            case ProductEditCurrencyType.USD:
                mainPriceTextView.setText(getString(R.string.usd_format, currencyString));
                break;
            default:
            case ProductEditCurrencyType.RUPIAH:
                mainPriceTextView.setText(getString(R.string.rupiah_format, currencyString));
                break;

        }
    }

    @Override
    public int getCurrencyType() {
        return currencyType;
    }

    private void initCurrency(@ProductEditCurrencyType int currencyType) {
        switch (currencyType) {
            case ProductEditCurrencyType.USD:
                this.currencyType = ProductEditCurrencyType.USD;
                break;
            default:
            case ProductEditCurrencyType.RUPIAH:
                this.currencyType = ProductEditCurrencyType.RUPIAH;
                break;
        }
    }

    public void setWholesalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        if (wholesalePrice != null)
            wholesaleAdapter.addAllWholeSalePrice(wholesalePrice);

        updateWholesaleButton();
    }

    private void updateWholesaleButton() {
        addWholesaleTextView.setVisibility(wholesaleAdapter.getItemCount() < MAX_WHOLESALE ? View.VISIBLE : View.GONE);
        if(wholesaleAdapter.getItemCount()==0){
            setButtonSubmit(false);
        }
    }

    @Override
    public void notifySizeChanged(int currentSize) {
        updateWholesaleButton();
    }

    @Override
    public void setButtonSubmit(boolean state) {
        buttonSave.setEnabled(state);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_wholesale, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete_wholesale) {
            if (hasVariant) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setTitle(getString(R.string.dialog_delete_wholesale_title))
                        .setMessage(getString(R.string.dialog_delete_wholesale_message))
                        .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                wholesaleAdapter.removeAll();
                                wholesaleAdapter.notifyDataSetChanged();
                                notifySizeChanged(wholesaleAdapter.getItemSize());
                                exitWholesaleActivity();
                            }
                        }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            } else {
                wholesaleAdapter.removeAll();
                wholesaleAdapter.notifyDataSetChanged();
                notifySizeChanged(wholesaleAdapter.getItemSize());
                exitWholesaleActivity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isAnyWholesaleChange() {
        productWholesaleViewModelList = wholesaleAdapter.getProductWholesaleViewModels();
        if (productWholesaleViewModelListTemp == null) {
            productWholesaleViewModelListTemp = new ArrayList<>();
        }

        boolean state = new Gson().toJson(productWholesaleViewModelList).equalsIgnoreCase(new Gson().toJson(productWholesaleViewModelListTemp));

        return state;
    }
}
