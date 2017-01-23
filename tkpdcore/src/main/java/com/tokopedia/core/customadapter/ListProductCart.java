//package com.tokopedia.core.customadapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputFilter;
//import android.text.Spanned;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.tkpd.library.utils.CommonUtils;
//import com.tkpd.library.utils.ImageHandler;
//import com.tokopedia.core.R;
//import com.tokopedia.core.fragment.FragmentCart;
//import com.tokopedia.core.product.activity.ProductInfoActivity;
//import com.tokopedia.core.util.MethodChecker;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class ListProductCart {
//
//    private ArrayList<String> pName = new ArrayList<String>();
//    private ArrayList<String> pPrice = new ArrayList<String>();
//    private ArrayList<String> pWeight = new ArrayList<String>();
//    private ArrayList<String> Notes = new ArrayList<String>();
//    private ArrayList<String> newNotes = new ArrayList<String>();
//    private ArrayList<String> ProdID = new ArrayList<String>();
//    private ArrayList<String> CartID = new ArrayList<String>();
//    private ArrayList<String> PriceTotal = new ArrayList<String>();
//    private ArrayList<String> ErrorMsg = new ArrayList<String>();
//    private ArrayList<String> ProdUrl = new ArrayList<String>();
//    private ArrayList<Integer> Qty = new ArrayList<Integer>();
//    private ArrayList<Integer> MinOrder = new ArrayList<Integer>();
//    private ArrayList<Integer> newQty = new ArrayList<Integer>();
//    private ArrayList<Boolean> isEditProd = new ArrayList<Boolean>();
//    private ArrayList<String> pImageUri = new ArrayList<String>();
//    private ArrayList<ViewHolder> Item = new ArrayList<ViewHolder>();
//    private ArrayList<Integer> preorderStatus = new ArrayList<>();
//    private ArrayList<String> preorderPeriod = new ArrayList<>();
//    private int State; //1 = able to edit, 2 = no edit
//    public Boolean isMain = false;
//    public Boolean isEdit = false;
//    private View CurrEditView;
//    public FragmentCart fragmentCart = null;
//    public Activity context;
//    public LayoutInflater inflater;
//    public LinearLayout lvcontainer;
//    public View view;
//
//    public ListProductCart(Activity context, LinearLayout lvcontainer, int State, ArrayList<String> CartID, ArrayList<String> pName, ArrayList<String> pImageUri, ArrayList<String> pPrice, ArrayList<String> pWeight,
//                           ArrayList<Integer> Qty, ArrayList<String> Notes, ArrayList<String> PriceTotal, ArrayList<String> ErrorMsg) {
//        super();
//
//        this.context = context;
//        this.pName = pName;
//        this.pPrice = pPrice;
//        this.pWeight = pWeight;
//        this.Notes = Notes;
//        this.PriceTotal = PriceTotal;
//        this.Qty = Qty;
//        this.pImageUri = pImageUri;
//        this.ErrorMsg = ErrorMsg;
//        this.CartID = CartID;
//        this.State = State;
//        this.lvcontainer = lvcontainer;
//
//
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        addItemToLayout();
//    }
//
//
//    public ListProductCart(Activity context, LinearLayout lvcontainer, FragmentCart fragmentCart, int State, ArrayList<String> CartID, ArrayList<String> pName, ArrayList<String> pImageUri, ArrayList<String> pPrice, ArrayList<String> pWeight,
//                           ArrayList<Integer> Qty, ArrayList<String> Notes, ArrayList<String> PriceTotal, ArrayList<String> ErrorMsg, ArrayList<Integer> MinOrder, ArrayList<String> ProdID, ArrayList<String> ProdUrl, ArrayList<Integer> preorderStatus,
//                           ArrayList<String> preorderPeriod) {
//
//        this.context = context;
//        this.pName = pName;
//        this.pPrice = pPrice;
//        this.Notes = Notes;
//        this.PriceTotal = PriceTotal;
//        this.Qty = Qty;
//        this.pImageUri = pImageUri;
//        this.ErrorMsg = ErrorMsg;
//        this.ProdID = ProdID;
//        this.CartID = CartID;
//        this.State = State;
//        this.lvcontainer = lvcontainer;
//        this.fragmentCart = fragmentCart;
//        this.MinOrder = MinOrder;
//        this.pWeight = pWeight;
//        this.ProdUrl = ProdUrl;
//        this.preorderStatus = preorderStatus;
//        this.preorderPeriod = preorderPeriod;
//        inflater = LayoutInflater.from(context);
//        addItemToLayout();
//
//    }
//
//
//    public static class ViewHolder {
//        ImageView pImageView;
//        TextView pNameView;
//        TextView pPriceView;
//        TextView pWeightView;
//        TextView PriceTotalView;
//        TextView pErrorMsgView;
//        EditText NotesView;
//        EditText QtyView;
//        View DeleteAreaBut;
//        ImageView DeleteBut;
//        TextView preorderLabel;
//        TextView preorderPeriodText;
//    }
//
//    public void addItemToLayout() {
//        for (int position = 0; position < pName.size(); position++) {
//            final int pos = position;
//            View view = inflater.inflate(R.layout.listview_product_cart, lvcontainer, false);
//            ViewHolder holder = new ViewHolder();
//            holder.pImageView = (ImageView) view.findViewById(R.id.img);
//            holder.pNameView = (TextView) view.findViewById(R.id.name);
//            holder.pPriceView = (TextView) view.findViewById(R.id.price);
//            holder.pWeightView = (TextView) view.findViewById(R.id.weight);
//            holder.PriceTotalView = (TextView) view.findViewById(R.id.price_total);
//            holder.pErrorMsgView = (TextView) view.findViewById(R.id.error_msg);
//            holder.NotesView = (EditText) view.findViewById(R.id.notes);
//            holder.QtyView = (EditText) view.findViewById(R.id.qty);
//            holder.DeleteAreaBut = (View) view.findViewById(R.id.delete_view);
//            holder.DeleteBut = (ImageView) view.findViewById(R.id.delete_but);
//            holder.preorderLabel = (TextView) view.findViewById(R.id.preorder_label);
//            holder.preorderPeriodText = (TextView) view.findViewById(R.id.preorder_period_text);
//
//            if (ErrorMsg.size() > 0) {
//                if (ErrorMsg.get(position) != null && ErrorMsg.get(position).length()>1) {
//                    holder.pErrorMsgView.setVisibility(View.VISIBLE);
//                    holder.pErrorMsgView.setText(ErrorMsg.get(position));
//                }
//            }
//            ImageHandler.loadImageRounded2(context, holder.pImageView, pImageUri.get(position));
////			ImageHandler.LoadImageRounded(holder.pImageView, pImageUri.get(position));
//            holder.pNameView.setText(MethodChecker.fromHtml(pName.get(position)));
//            holder.pPriceView.setText(pPrice.get(position));
//            holder.pWeightView.setText(pWeight.get(position));
//            holder.PriceTotalView.setText(PriceTotal.get(position));
//            holder.NotesView.setFilters(new InputFilter[]{inputFilter()});
//            holder.NotesView.setText(Notes.get(position));
//            holder.QtyView.setText(Integer.toString(Qty.get(position)));
//            if(!preorderStatus.isEmpty() && preorderStatus.get(position)!=0){
//                holder.preorderLabel.setVisibility(View.VISIBLE);
//                holder.preorderPeriodText.setVisibility(View.VISIBLE);
//                holder.preorderPeriodText.setText(context.
//                        getString(R.string.hint_preorder_text).
//                        replace("YYY", preorderPeriod.get(position)));
//            }else{
//                holder.preorderLabel.setVisibility(View.GONE);
//                holder.preorderPeriodText.setVisibility(View.GONE);
//            }
//            if (State == 1) {
//                holder.pNameView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//
//                holder.QtyView.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//                    @Override
//                    public void onFocusChange(View arg0, boolean arg1) {
//                        CurrEditView = arg0;
//
//                    }
//
//                });
//                holder.QtyView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        TriggerEdit();
//                    }
//
//                });
//                holder.NotesView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        TriggerEdit();
//                    }
//
//                });
//                holder.NotesView.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//                    @Override
//                    public void onFocusChange(View arg0, boolean arg1) {
//                        CurrEditView = arg0;
//
//                    }
//
//                });
//                holder.pNameView.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
////						Intent intent = new Intent(context, ProductDetailPresenter.class);
//                        Intent intent = new Intent(context, ProductInfoActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("product_id", ProdID.get(pos));
//                        bundle.putString("product_uri", ProdUrl.get(pos));
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//                    }
//                });
//                holder.DeleteBut.setOnClickListener(new DeleteProd(position));
//
//                if (isEdit) {
//                    holder.QtyView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_cart_1));
//                    holder.NotesView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_cart_1));
//                    holder.QtyView.setEnabled(true);
//                    holder.NotesView.setEnabled(true);
//                } else {
//                    holder.QtyView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                    holder.NotesView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                    holder.QtyView.setEnabled(false);
//                    holder.NotesView.setEnabled(false);
//                }
//                isEditProd.add(false);
//                newQty.add(0);
//                newNotes.add("");
//
//            }
//
//            if (State == 2) {
//                holder.DeleteAreaBut.setVisibility(View.GONE);
//                holder.QtyView.setEnabled(false);
//                holder.NotesView.setEnabled(false);
//            }
//
//            Item.add(holder);
//            lvcontainer.addView(view);
//        }
//        CommonUtils.dumper("Lewat Sini");
//
//
//    }
//
//    public void TriggerEdit() {
//        isEdit = true;
//        for (int i = 0; i < Item.size(); i++) {
//            Item.get(i).QtyView.setBackground(context.getResources().getDrawable(R.drawable.border_cart_1));
//            Item.get(i).NotesView.setBackground(context.getResources().getDrawable(R.drawable.border_cart_1));
//            Item.get(i).QtyView.setEnabled(true);
//            Item.get(i).NotesView.setEnabled(true);
//        }
//    }
//
//    public void CancelEdit() {
//        isEdit = false;
//        for (int i = 0; i < Item.size(); i++) {
//            Item.get(i).QtyView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//            Item.get(i).NotesView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//            Item.get(i).QtyView.setEnabled(false);
//            Item.get(i).NotesView.setEnabled(false);
//        }
//    }
//
//    public String GetEditData() {
//        JSONArray arrItem = new JSONArray();
//        Boolean isError = false;
//        for (int i = 0; i < Item.size(); i++) {
//            Boolean itemError = false;
//            String string = Item.get(i).QtyView.getText().toString();
//            if (string.equals("")) {
//                Item.get(i).QtyView.setError(context.getString(R.string.error_field_required));
//                isError = true;
//                itemError = true;
//            } else if (Integer.parseInt(string) < MinOrder.get(i)) {
//                Item.get(i).QtyView.setError(context.getString(R.string.error_min_order) + " " + MinOrder.get(i));
//                isError = true;
//                itemError = true;
//            }
//            String notesText = Item.get(i).NotesView.getText().toString();
//            if (notesText.length() > 144){
//                isError = true;
//                itemError = true;
//                Item.get(i).NotesView.setError(context.getString(R.string.error_max_notes_length));
//            }
//            if (!itemError){
//                isEditProd.set(i, true);
//                newQty.set(i, Integer.parseInt(string));
//                newNotes.set(i, notesText);
//            }
//        }
//        if (!isError) {
//            for (int i = 0; i < isEditProd.size(); i++) {
//                JSONObject item = new JSONObject();
//                if (isEditProd.get(i)) {
//                    Qty.set(i, newQty.get(i));
//                    Notes.set(i, newNotes.get(i));
//                    try {
//                        item.put("product_cart_id", CartID.get(i));
//                        item.put("product_notes", Notes.get(i));
//                        item.put("product_quantity", Qty.get(i));
//                        arrItem.put(item);
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//            isEdit = false;
//            for (int i = 0; i < Item.size(); i++) {
//                Item.get(i).QtyView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                Item.get(i).NotesView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                Item.get(i).QtyView.setEnabled(false);
//                Item.get(i).NotesView.setEnabled(false);
//            }
//            return arrItem.toString();
//        }
//        return null;
//    }
//
//    public class DeleteProd implements OnClickListener {
//        int position;
//
//        public DeleteProd(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (fragmentCart != null) {
//                fragmentCart.DeleteProduct(CartID.get(position));
//            }
//
//        }
//
//    }
//
//    public void RefreshView(ArrayList<String> CartID, ArrayList<String> pName, ArrayList<String> pImageUri, ArrayList<String> pPrice, ArrayList<String> pWeight,
//                            ArrayList<Integer> Qty, ArrayList<String> Notes, ArrayList<String> PriceTotal, ArrayList<String> ErrorMsg, ArrayList<Integer> MinOrder,
//                            ArrayList<String> productURL, ArrayList<Integer> preorderStatus, ArrayList<String> preorderPeriod, ArrayList<String> productID) {
//
//        this.pName = pName;
//        this.pPrice = pPrice;
//        this.Notes = Notes;
//        this.PriceTotal = PriceTotal;
//        this.Qty = Qty;
//        this.pImageUri = pImageUri;
//        this.ErrorMsg = ErrorMsg;
//        this.CartID = CartID;
//        this.MinOrder = MinOrder;
//        this.pWeight = pWeight;
//        this.preorderStatus = preorderStatus;
//        this.preorderPeriod = preorderPeriod;
//        this.ProdUrl = productURL;
//        this.ProdID = productID;
//
//        Item.clear();
//        lvcontainer.removeAllViews();
//        lvcontainer.invalidate();
//
//        addItemToLayout();
//
//    }
//
//    private InputFilter inputFilter() {
//        return new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                for (int index = start; index < end; index++) {
//                    int type = Character.getType(source.charAt(index));
//
//                    if (type == Character.SURROGATE) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        };
//    }
//
//}
