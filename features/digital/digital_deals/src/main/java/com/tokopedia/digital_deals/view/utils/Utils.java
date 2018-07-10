package com.tokopedia.digital_deals.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;
import com.tokopedia.digital_deals.view.model.FilterItem;
import com.tokopedia.digital_deals.view.model.ValuesItem;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.tokopedia.digital_deals.view.utils.Utils.Constants.DIGITAL_DEALS;


public class Utils {
    private static Utils singleInstance;
    private static Location location;
    public static String BRAND_QUERY_PARAM_TREE = "tree";
    public static String BRAND_QUERY_PARAM_BRAND = "brand";
    public static String BRAND_QUERY_PARAM_CHILD_CATEGORY_ID = "child_category_ids";
    public static String BRAND_QUERY_PARAM_CITY_ID = "cities";
    public static String BRAND_QUERY_PARAM_LOCATION_ID= "location_id";
    private float defaultBitmapScale = 0.1f;
    private static final float MAX_RADIUS = 25.0f;
    private static final float MIN_RADIUS = 0.0f;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public ArrayList<CategoryItem> convertIntoCategoryListViewModel(DealsResponse dealsResponse) {

        ArrayList<CategoryItem> categoryRespons = new ArrayList<>();
        if (dealsResponse.getHome()!=null && dealsResponse.getCategoryItems()!= null) {
            for (CategoryItem categoryItem : dealsResponse.getCategoryItems()) {

                CategoryItem category = new CategoryItem();
                category.setTitle(categoryItem.getTitle());
                category.setCategoryId(categoryItem.getCategoryId());
                category.setCount(categoryItem.getCount());
                category.setName(categoryItem.getName());
                category.setMediaUrl(categoryItem.getMediaUrl());
                category.setUrl(categoryItem.getUrl());
                category.setItems(categoryItem.getItems());

                switch (categoryItem.getName().toLowerCase()) {
                    case "top":
                        categoryRespons.add(0, category);
                        break;
                    case "carousel":
                        categoryRespons.add(0, category);
                        break;
                    default:
                        categoryRespons.add(category);
                        break;
                }
            }

            applyFilterOnCategories(categoryRespons, dealsResponse.getFilters());


        }
        return categoryRespons;
    }

    private void applyFilterOnCategories(ArrayList<CategoryItem> categoryRespons, List<FilterItem> filters) {
        Map<Integer, Integer> sortOrder = new HashMap<>();
        if (filters != null) {
            if(categoryRespons.get(0).getCategoryId()== categoryRespons.get(1).getCategoryId()){
                categoryRespons.get(1).setCategoryId(-1);            //Since carousel and top have same id's
            }
            sortOrder.put(categoryRespons.get(0).getCategoryId(), -1);   //dummy for top or carousel
            sortOrder.put(categoryRespons.get(1).getCategoryId(), -2);   //dummy for top or carousel
            for (FilterItem filter : filters) {
                if (filter.getAttributeName().equals("child_category_ids")) {
                    if (filter.getValues() != null) {
                        for (ValuesItem value : filter.getValues()) {
                            sortOrder.put(value.getId(), value.getPriority());
                        }
                    }
                    if(sortOrder.size()== categoryRespons.size()){
                        Collections.sort(categoryRespons, new CategoryItemComparator(sortOrder));
                    }
                }
            }
        }
    }

    private class CategoryItemComparator implements Comparator<CategoryItem> {
        private Map<Integer, Integer> sortOrder;

        public CategoryItemComparator(Map<Integer, Integer> sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(CategoryItem i1, CategoryItem i2) {
            Integer id1 = sortOrder.get(i1.getCategoryId());
            if (id1 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i1.getCategoryId());
            }
            Integer id2 = sortOrder.get(i2.getCategoryId());
            if (id2 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i2.getCategoryId());
            }
            return id1.compareTo(id2);
        }
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }


    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static Locale locale = new Locale("in", "ID");
    public static final String RUPIAH_FORMAT = "Rp %s";

    public static String convertToCurrencyString(Integer value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value.longValue()));
    }

    public static String convertToCurrencyString(long value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value));
    }

    public Location getLocation(Context context) {

        if (location == null) {
            final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
            String locationjson = localCacheHandler.getString(TkpdCache.Key.KEY_DEALS_LOCATION, null);
            if (locationjson != null) {
                Gson gson = new Gson();
                location = gson.fromJson(locationjson, Location.class);
            }
        }
        return location;
    }

    public void updateLocation(Context context, Location locatn) {

        location = locatn;
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
        Gson gson = new Gson();
        String json = gson.toJson(location);
        localCacheHandler.putString(TkpdCache.Key.KEY_DEALS_LOCATION, json);
        localCacheHandler.applyEditor();
    }

    public void openGoogleMapsActivity(Context context, String latLng) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latLng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.cannot_find_application), Toast.LENGTH_SHORT).show();
        }
    }

    public void setSnackBarLocationChange(String locationName, Context context, ViewGroup coordinatorLayout) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, locationName, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_location_change_snackbar, null);
        TextView tv = snackView.findViewById(R.id.tv_location_ame);
        tv.setText(locationName.toUpperCase());
        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }

    public void shareDeal(String deeplinkSlug, Context context, String name, String imageUrl) {

        ShareData shareData = ShareData.Builder.aShareData()
                .setType("")
                .setName(name)
                .setUri(DIGITAL_DEALS + "/" + deeplinkSlug)
                .setImgUri(imageUrl)
                .build();
        BranchSdkUtils.generateBranchLink(shareData, (Activity) context, new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TEXT, branchUrl);
                context.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap setBlur(Bitmap smallBitmap, float radius, Context context) {
        if (radius > MIN_RADIUS && radius <= MAX_RADIUS) {

//            int width = Math.round(smallBitmap.getWidth() * defaultBitmapScale);
//            int height = Math.round(smallBitmap.getHeight() * defaultBitmapScale);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(smallBitmap, smallBitmap.getWidth(), smallBitmap.getHeight(), false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript renderScript = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
            theIntrinsic.setRadius(radius);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }else{
            return smallBitmap;
        }
    }


    public static class Constants {

        public final static String DEALS = "deals";
        public static final String DIGITAL_DEALS = "tokopedia://deals";
        public static final String DIGITAL_DEALS_DETAILS = "tokopedia://deals/{slug}";
    }

    public static String fetchOrderId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}
