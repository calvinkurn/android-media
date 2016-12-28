package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.myproduct.model.ActResponseModelData;
import com.tokopedia.core.myproduct.model.EditPriceParam;
import com.tokopedia.core.myproduct.model.ManageProductModel;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.presenter.NetworkInteractor;
import com.tokopedia.core.myproduct.presenter.NetworkInteractorImpl;
import com.tokopedia.core.myproduct.utils.VerificationUtils;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

public class ListViewManageProdAdapter extends BaseAdapter
        implements
        NetworkInteractorImpl.EditPrice {
    public static final String STUART = "STUART";
    public boolean IsLoading = false;
    public int isProdManager;
    private TkpdProgressDialog mProgressDialog;
    public Activity context;
    public LayoutInflater inflater;
    private ArrayList<ManageProductModel> manageProductModels;
    private Set<Integer> Checked = new HashSet<>();
    private ArrayList<String> EditMode = new ArrayList<String>();

    // NEW NETWORK
    NetworkInteractor networkInteractorImpl;
    private boolean multiselect;

    public ListViewManageProdAdapter(Activity context, int isProductManager) {
        super();
        mProgressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        this.context = context;
        isProdManager = isProductManager;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        manageProductModels = new ArrayList<>();
        networkInteractorImpl = new NetworkInteractorImpl();
        multiselect = false;
    }

    public void setData(ArrayList<ManageProductModel> manageProductModels) {
        this.manageProductModels = manageProductModels;
        notifyDataSetChanged();
    }

    public void setProdManager(int isProdManager) {
        this.isProdManager = isProdManager;
        notifyDataSetChanged();
    }

    public void clearEditMode() {
        manageProductModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return manageProductModels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public void clearDatas() {
        manageProductModels.clear();
        Checked.clear();
        EditMode.clear();

        notifyDataSetChanged();
    }

    public void clearDatas2() {
        manageProductModels.clear();

        notifyDataSetChanged();
    }

    public void addCheckedItem(int position) {
        Checked.add(position);
    }

    public boolean removeCheckedItem(int position) {
        return Checked.remove(position);
    }

    public List<Integer> CheckdItems() {
        return new ArrayList<Integer>(Checked);
    }

    public List<String> CheckedProductId() {
        List<String> checkedProductIds = new ArrayList<>();
        for (int checked : CheckdItems()) {
            checkedProductIds.add(manageProductModels.get(checked).getProdID());
        }
        return checkedProductIds;
    }

    public void clearCheckdData() {
        Checked.clear();

        notifyDataSetChanged();
    }

    public String getProductId(int position) {
        return manageProductModels.get(position).getProdID();
    }

    public ArrayList<String> getMenuName(String mAddTo) {
        ArrayList<String> menuName = new ArrayList<>();
        menuName.add(context.getString(R.string.title_update_etalase));
        List<EtalaseDB> etalaseDBs = DbManagerImpl.getInstance().getEtalases();
        for (EtalaseDB etalaseDB :
                etalaseDBs) {
            menuName.add(String.valueOf(MethodChecker.fromHtml(etalaseDB.getEtalaseName())));
        }
//		if (mAddTo.equals("1")){
        menuName.add(context.getString(R.string.title_add_new_etalase));
//		}
        return menuName;
    }

    public String getMenuId(int position) {
        List<EtalaseDB> etalaseDBs = DbManagerImpl.getInstance().getEtalases();
        return etalaseDBs.get(position).getEtalaseId() + "";
    }

    public int getCheckSize() {
        return Checked.size();
    }

    public ArrayList<ManageProductModel> getManageProductModels() {
        return manageProductModels;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onFailureEditPrice(String e, EditPriceParam param) {
        mProgressDialog.dismiss();
        String message = CommonUtils.generateMessageError(context, e);
        showSnackBar(message);

    }

    private void showSnackBar(String err) {
        SnackbarManager.make(context, err, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessEditPrice(Response<TkpdResponse> responseData) {
        TkpdResponse response = responseData.body();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.getStringData());
        } catch (JSONException je) {
            Log.e(STUART, je.getLocalizedMessage());
        }

        Gson gson = new Gson();

        ActResponseModelData data =
                gson.fromJson(jsonObject.toString(), ActResponseModelData.class);

        IsLoading = false;
        mProgressDialog.dismiss();

        if (data.getIsSuccess() == 1) {
            showSnackBar(context.getString(R.string.title_quick_success));
            KeyboardHandler keyboardHandler = new KeyboardHandler();
            keyboardHandler.hideSoftKeyboard(context);
            clearCheckdData();
            ((ManageProduct) context).ClearData();
            ((ManageProduct) context).CheckCache();
        }

    }

    public static class ViewHolder {
        ImageView pImageView;
        TextView pNameView;
        TextView EditBut;
        TextView CopyBut;
        TextView Etalase;
        TextView SaveBut;
        TextView Dept;
        TextView UnderRev;
        View EditorArea;
        View MainView;
        Spinner Currency;
        EditText Prices;
        TextView PriceView;
        int TempPriceLength;
        View ButOverflow;
        TextView GudangTag;
        TextView returnableView;
        //		CardView itemManageProduct;
        int position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_manage_product, null);
            holder.ButOverflow = convertView.findViewById(R.id.but_overflow);
            holder.pImageView = (ImageView) convertView.findViewById(R.id.prod_img);
            holder.SaveBut = (TextView) convertView.findViewById(R.id.save_button);
            holder.pNameView = (TextView) convertView.findViewById(R.id.prod_name);
            holder.PriceView = (TextView) convertView.findViewById(R.id.price_view);
            holder.Etalase = (TextView) convertView.findViewById(R.id.etalase);
            holder.Dept = (TextView) convertView.findViewById(R.id.dept);
            holder.EditBut = (TextView) convertView.findViewById(R.id.edit_but);
            holder.CopyBut = (TextView) convertView.findViewById(R.id.copy_but);
            holder.UnderRev = (TextView) convertView.findViewById(R.id.review_warning);
            holder.EditorArea = (View) convertView.findViewById(R.id.editor_area);
            holder.Prices = (EditText) convertView.findViewById(R.id.price);
            holder.Currency = (Spinner) convertView.findViewById(R.id.currency);
            holder.MainView = convertView.findViewById(R.id.main_view);
            holder.GudangTag = (TextView) convertView.findViewById(R.id.tag_gudang);
            holder.returnableView = (TextView) convertView.findViewById(R.id.returnable_list_product);
//			holder.itemManageProduct = (CardView) convertView.findViewById(R.id.item_manage_product);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ManageProductModel manageProductModel = manageProductModels.get(position);
        String productImage = manageProductModel.getProdImgUri();
        final String ProductID = manageProductModel.getProdID();
        String DepName = manageProductModel.getDepName();
        String Etalase = manageProductModel.getEtalaseLoc();
        int CurrencyCode = manageProductModel.getCurrencyCodeInt();
        String price = manageProductModel.getPrice();
        String prodName = manageProductModel.getProdName();
        Integer returnablePolicy = manageProductModel.getReturnablePolicy();
        String Status = manageProductModel.getPStatus();

        if (!productImage.equals("null")) {
            ImageHandler.loadImageRounded2(context, holder.pImageView, productImage);
        }

        if (isProdManager == 0) {
            holder.ButOverflow.setVisibility(View.GONE);
        }

        if (new ArrayList<>(Checked).contains(position)) {
            holder.MainView.setBackgroundResource(R.drawable.cards_ui_select);
        } else {
            holder.MainView.setBackgroundResource(R.drawable.cards_ui_selected);
        }

        if (EditMode.contains(ProductID)) {
            holder.Prices.setVisibility(View.VISIBLE);
            holder.PriceView.setVisibility(View.GONE);
            holder.Currency.setVisibility(View.VISIBLE);
            holder.SaveBut.setVisibility(View.VISIBLE);
        } else {
            holder.Prices.setVisibility(View.GONE);
            holder.Currency.setVisibility(View.GONE);
            holder.PriceView.setVisibility(View.VISIBLE);
            holder.SaveBut.setVisibility(View.GONE);
        }
        holder.Dept.setText(DepName);
        if (!Etalase.equals(context.getResources().getString(R.string.title_warehouse))) {
            holder.Etalase.setVisibility(View.VISIBLE);
            if (Etalase.equals("Stok Kosong")) {
                holder.GudangTag.setVisibility(View.VISIBLE);
            } else {
                holder.GudangTag.setVisibility(View.GONE);
            }
            holder.Etalase.setText(context.getString(R.string.prompt_etalase) + ": " + MethodChecker.fromHtml(Etalase));
        } else {
            holder.Etalase.setVisibility(View.GONE);
            holder.GudangTag.setVisibility(View.VISIBLE);
        }


        holder.Currency.setSelection(CurrencyCode - 1);

//			if (ContainWholesale.get(holder.position))

        if (SessionHandler.isGoldMerchant(context)) {
            holder.Currency.setEnabled(true);
        } else {
            holder.Currency.setEnabled(false);
        }
//			else
//				holder.Currency.setEnabled(true);

        holder.Currency.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                ManageProductModel manageProductModel1 = manageProductModels.get(position);
                manageProductModel.setCurrencyCode((arg2 + 1) + "");
                manageProductModels.set(position, manageProductModel);
                if (arg2 + 1 == 1) {
                    if (holder.Prices.getText().toString().contains("."))
                        holder.Prices.setText(manageProductModel.getPrice());
                } else {
                    holder.Prices.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        holder.Prices.setText(price);

        if (CurrencyCode == 1) {
            holder.PriceView.setText("Rp. " + price);
        } else {
            holder.PriceView.setText("$ " + price);
        }

        holder.PriceView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!multiselect) {
                    holder.Prices.setVisibility(View.VISIBLE);
                    holder.PriceView.setVisibility(View.GONE);
                    holder.Currency.setVisibility(View.VISIBLE);
                    holder.SaveBut.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.Prices.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (position < manageProductModels.size()) {
                    String currencycode = manageProductModels.get(position).getCurrencyCode().equals("1") ? "Rp" : "US$";
                    ImageGalleryImpl.Pair<Boolean, String> verif = VerificationUtils.validatePrice(context, currencycode, s.toString());
                    if (!verif.getModel1()) {
                        holder.Prices.setError(verif.getModel2());
                    } else {
                        holder.Prices.setError(null);
                        if (holder.Prices.getText().toString().contains("\n")) {
                            holder.Prices.setText(holder.Prices.getText().toString().replace("\n", ""));
                        }
                        if (currencycode.contains("Rp")) {
                            CurrencyFormatHelper.SetToRupiah(holder.Prices);
                        } else {
                            CurrencyFormatHelper.SetToDollar(holder.Prices);
                        }
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        holder.Prices.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER && !IsLoading) {
//					holder.Prices.setVisibility(View.GONE);
//					holder.Currency.setVisibility(View.GONE);
//					holder.PriceView.setVisibility(View.VISIBLE);
//					holder.SaveBut.setVisibility(View.GONE);
//					EditPrice(position, holder.Currency.getSelectedItemPosition()+1, holder.Prices.getText().toString());
                }
                return false;
            }
        });
        holder.SaveBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!IsLoading) {
                    holder.Prices.setVisibility(View.GONE);
                    holder.Currency.setVisibility(View.GONE);
                    holder.PriceView.setVisibility(View.VISIBLE);
                    holder.SaveBut.setVisibility(View.GONE);
                    KeyboardHandler.DropKeyboard(context, holder.MainView);
                    editPrice(position, holder.Currency.getSelectedItemPosition() + 1, holder.Prices.getText().toString());
                }
            }
        });
        holder.pNameView.setText(MethodChecker.fromHtml(prodName));
        holder.returnableView.setText(returnableCondition(returnablePolicy));
        if (returnablePolicy != 1) {
            holder.returnableView.setVisibility(View.GONE);
        }
        holder.EditBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//				Intent intent = new Intent(context, EditProduct.class);
//				bundle.putString("product_id", ProductID);
//				bundle.putBoolean("is_edit", true);
//				intent.putExtras(bundle);
//				context.startActivityForResult(intent, 1);
                if (!multiselect) {
                    boolean isEdit = true;
                    Intent intent = ProductActivity.moveToEditFragment(context, isEdit, ProductID);
                    context.startActivityForResult(intent, 1);
                }

            }

        });

        if (Status.equals(context.getString(R.string.PRD_STATE_PENDING))) {
            holder.CopyBut.setVisibility(View.GONE);
            holder.EditBut.setVisibility(View.GONE);
            holder.UnderRev.setVisibility(View.VISIBLE);
        }

        if (isProdManager == 0) {
            holder.EditorArea.setVisibility(View.GONE);
        } else {
            holder.EditorArea.setVisibility(View.VISIBLE);
        }

        holder.CopyBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Bundle bundle = new Bundle();
//				Intent intent = new Intent(context, EditProduct.class);
//				bundle.putString("product_id", ProductID);
//				bundle.putBoolean("is_edit", false);
//				intent.putExtras(bundle);
//				context.startActivityForResult(intent, 1);
                if (!multiselect) {
                    boolean isEdit = true;
                    Intent intent = ProductActivity.moveToEditFragment(context, isEdit, ProductID);
                    context.startActivityForResult(intent, 1);
                }
            }
        });

        holder.ButOverflow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!multiselect) {
                    showPopup(v, position, holder);
                }
            }
        });

//		holder.itemManageProduct.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Bundle bundle = new Bundle();
//				//				Intent intent = new Intent(ManageProduct.this, ProductDetailPresenter.class);
//				Intent intent = new Intent(context, ProductInfoActivity.class);
//				bundle.putString("product_id", getProductId(position));
//				intent.putExtras(bundle);
//				context.startActivityForResult(intent, 2);
//			}
//		});
        return convertView;
    }

    private boolean isValidPrice(ViewHolder holder) {
        if (holder.Prices.getText().length() > 0 && Integer.parseInt(CurrencyFormatHelper.RemoveNonNumeric(holder.Prices.getText().toString())) > 0)
            return true;
        else {
            holder.Prices.setError(context.getString(R.string.error_field_required));
            holder.Prices.requestFocus();
            return false;
        }
    }

    private void showPopup(View v, final int position, final ViewHolder holder) {

        final ManageProductModel manageProductModel = manageProductModels.get(position);
        final String ProductID = manageProductModel.getProdID();

        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.manage_product_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CommonUtils.dumper(item.getItemId());
                Intent intent;
                Bundle bundle;
                boolean isEdit;
                boolean isCopy;
                if (item.getItemId() == R.id.action_edit) {//			        	bundle = new Bundle();
//						intent = new Intent(context, EditProduct.class);
//						bundle.putString("product_id", ProductID);
//						bundle.putBoolean("is_edit", true);
//						intent.putExtras(bundle);
//						context.startActivityForResult(intent, 1);
                    isEdit = true;
                    intent = ProductActivity.moveToEditFragment(context, isEdit, ProductID);
                    context.startActivityForResult(intent, 1);
                    return true;
                } else if (item.getItemId() == R.id.action_copy) {//			        	bundle = new Bundle();
//						intent = new Intent(context, EditProduct.class);
//						bundle.putString("product_id", ProductID);
//						bundle.putBoolean("is_edit", false);
//						intent.putExtras(bundle);
//						context.startActivityForResult(intent, 1);
                    isCopy = true;
                    intent = ProductActivity.moveToCopyFragment(context, isCopy, ProductID);
                    context.startActivityForResult(intent, 1);
                    return true;
                } else if (item.getItemId() == R.id.action_edit_price) {
                    holder.Prices.setVisibility(View.VISIBLE);
                    holder.PriceView.setVisibility(View.GONE);
                    holder.Currency.setVisibility(View.VISIBLE);
                    holder.SaveBut.setVisibility(View.VISIBLE);
                    EditMode.add(ProductID);
                    return true;
                } else {
                    return false;
                }
            }

        });
        popup.show();
    }

    public boolean CloseQuickEdit() {
        if (EditMode.size() > 0) {
            EditMode.clear();
            notifyDataSetChanged();
            return false;
        } else
            return true;
    }

    private String returnableCondition(int condition) {
        String returnableInfo;
        if (condition == 1)
            returnableInfo = context.getString(R.string.table_policy_option);
        else if (condition == 2)
            returnableInfo = context.getString(R.string.table_policy_option_no);
        else {
            returnableInfo = context.getString(R.string.return_no_policy);
        }
        return returnableInfo;
    }


    private void editPrice(final int position, final int currency, final String price) {

        IsLoading = true;
        mProgressDialog.showDialog();

        ManageProduct manageProduct = null;
        if (checkNotNull((context)) && context instanceof ManageProduct) {
            manageProduct = (ManageProduct) context;
        }

        ((NetworkInteractorImpl) networkInteractorImpl).setEditPrice(this);
        ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(manageProduct.compositeSubscription);

        EditPriceParam param = new EditPriceParam();
        param.setCurrency(Integer.toString(currency));
        param.setPrice(CurrencyFormatHelper.RemoveNonNumeric(price));
        param.setProductId(manageProductModels.get(position).getProdID());
        param.setShopId(SessionHandler.getShopID(context));

        networkInteractorImpl.editPrice(context, param);
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public ProductPass getProductDataToPass(int position) {
        return ProductPass.Builder.aProductPass()
                .setProductPrice(manageProductModels.get(position).getPrice())
                .setProductId(manageProductModels.get(position).getProdID())
                .setProductName(manageProductModels.get(position).getProdName())
                .setProductImage(manageProductModels.get(position).getProdImgUri())
                .build();
    }

}
