package com.tokopedia.core.customadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ExpandableListCatalogSellerAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ImageView sellerImg;
    private ImageView sellerRating;
    private ExpandableListView _exp;

    public ExpandableListCatalogSellerAdapter(Context context, List<String> listDataHeader,
                                              HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        //this._exp = exp;

        Log.i("data_header", listDataHeader.toString());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(Integer.toString(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_catalog_seller_product, null);
        }

        try {
            JSONObject childTextData = new JSONObject(childText);

            TextView txtListChild = (TextView) convertView.findViewById(R.id.product_name);
            TextView prodCondition = (TextView) convertView.findViewById(R.id.prod_condition);
            TextView prodPrice = (TextView) convertView.findViewById(R.id.prod_price);
            final TextView prodId = (TextView) convertView.findViewById(R.id.product_id);
            //TextView btnBuy = (TextView) convertView.findViewById(R.shopId.btn_buy);
            View MainView = convertView.findViewById(R.id.main_view);

            MainView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
//					Intent intent = new Intent(_context, ProductDetailPresenter.class);
                    Intent intent = new Intent(_context, ProductInfoActivity.class);
                    bundle.putString("product_id", prodId.getText().toString());
                    intent.putExtras(bundle);
                    _context.startActivity(intent);


                }
            });

            txtListChild.setText(Html.fromHtml(childTextData.getString("name")));
            if (!childTextData.isNull("condition")) {
                if(childTextData.getString("condition").equals(("1")))
                    prodCondition.setText(_context.getString(R.string.title_new));
                else
                    prodCondition.setText(_context.getString(R.string.title_used));
            } else {
                prodCondition.setText(childTextData.getString("p_cond"));
            }
            if (!childTextData.isNull("price")) {
                prodPrice.setText(childTextData.getString("price"));
            } else {
                prodPrice.setText(childTextData.getString("p_price_fmt"));
            }
            prodId.setText(childTextData.getString("id"));
            ExpandableListView elv = (ExpandableListView) parent;
            elv.setCacheColorHint(0);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(Integer.toString(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_catalog_seller, null);
        }

        try {
            final JSONObject headerData = new JSONObject(headerTitle);

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.seller_name);
            TextView sellerLoc = (TextView) convertView.findViewById(R.id.seller_loc);
            sellerRating = (ImageView) convertView.findViewById(R.id.seller_rating);
            sellerImg = (ImageView) convertView.findViewById(R.id.seller_img);

            ImageHandler.LoadImage(sellerRating, headerData.getString("reputation_image_uri"));
            //ReputationLevelUtils.setReputationMedals(_context, sellerRating, getMedalType(rating), getMedalLevel(rating), getScoreMedal(rating));

            if (!headerData.isNull("city")) {
                sellerLoc.setText(headerData.getString("city"));
            } else if (!headerData.isNull("s_loc")) {
                sellerLoc.setText(headerData.getString("s_loc"));
            }
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(Html.fromHtml(headerData.getString("name")));
            lblListHeader.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(_context, ShopInfoActivity.class);
                    try {
                        intent.putExtras(ShopInfoActivity.createBundle(headerData.getString("id"), ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    _context.startActivity(intent);

                }
            });

//	        new GetImage(1,1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, headerData.getString("s_img"));
            ImageHandler.loadImageCircle2(_context, sellerImg, headerData.getString("image_uri"));
//            ImageHandler.LoadImageCircle(sellerImg, headerData.getString("s_img"));

            ExpandableListView elv = (ExpandableListView) parent;
            elv.expandGroup(groupPosition);
            elv.setOnGroupClickListener(new OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ExpandableListView elv = (ExpandableListView) parent;
        elv.expandGroup(groupPosition);

        return convertView;
    }

    private int getMedalType(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("reputation_set");
    }

    private int getMedalLevel(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("reputation_level");
    }

    private String getScoreMedal(JSONObject json) throws JSONException {
        return json.getString("reputation_score");
    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {

        private int state;
        private int pos;

        public GetImage(int state, int pos) {
            this.pos = pos;
            this.state = state;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            if (params[0] != null) {
                for (int i = 1; i <= 1; i++) {
                    try {
                        Log.i("params image", params[0]);
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
                        //Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.e("PROTOCOL", params[0]);
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap bitmap) {
            sellerImg.setImageBitmap(bitmap);
        }

    }

    private int GenerateRating(int param) {
        int RatingID = 0;
        switch (param) {
            case 0:
                RatingID = R.drawable.ic_star_none;
                break;
            case 1:
                RatingID = R.drawable.ic_star_one;
                break;
            case 2:
                RatingID = R.drawable.ic_star_two;
                break;
            case 3:
                RatingID = R.drawable.ic_star_three;
                break;
            case 4:
                RatingID = R.drawable.ic_star_four;
                break;
            case 5:
                RatingID = R.drawable.ic_star_five;
                break;
        }
        return RatingID;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}