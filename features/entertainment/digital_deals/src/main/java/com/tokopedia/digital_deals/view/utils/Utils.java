package com.tokopedia.digital_deals.view.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {
    private static Utils singleInstance;
    private static Location location;
    public static String LOCATION_COORDINATES = "coordinates";
    public static String QUERY_PARAM_CITY_ID = "cities";
    public static String LOCATION_NAME = "Jakarta";
    public static int LOCATION_ID = 318;
    public static int MAX_ITEMS_FOR_GA = 5;
    public static final String NEXT_URL = "nexturl";
    private static final float MAX_RADIUS = 25.0f;
    private static final float MIN_RADIUS = 0.0f;
    public static Locale locale = new Locale("in", "ID");
    private static final String RUPIAH_FORMAT = "Rp %s";
    private SparseIntArray likedEventMap;
    private SparseIntArray unLikedEventMap;
    public static final String NSQ_SERVICE = "Recommendation_For_You";
    public static final String NSQ_USE_CASE = "24";

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
    }

    public void addLikedEvent(int id, int currentLikes) {
        if (likedEventMap == null)
            likedEventMap = new SparseIntArray();
        likedEventMap.put(id, currentLikes + 1);
        removeUnlikedEvent(id);
    }

    public void removeLikedEvent(int id) {
        if (likedEventMap != null && likedEventMap.size() > 0) {
            int likes = likedEventMap.get(id);
            likedEventMap.delete(id);
            addUnlikedEvent(id, likes);
        }
    }

    public int containsLikedEvent(int id) {
        if (likedEventMap != null && likedEventMap.size() > 0)
            return likedEventMap.get(id, -1);
        return -1;
    }

    public int containsUnlikedEvent(int id) {
        if (unLikedEventMap != null && unLikedEventMap.size() > 0)
            return unLikedEventMap.get(id, -1);
        return -1;
    }

    private void addUnlikedEvent(int id, int likes) {
        if (unLikedEventMap == null)
            unLikedEventMap = new SparseIntArray();
        unLikedEventMap.put(id, likes - 1);
    }

    private void removeUnlikedEvent(int id) {
        if (unLikedEventMap != null && unLikedEventMap.size() > 0) {
            unLikedEventMap.delete(id);
        }

    }

    public void sortOutletsWithLocation(List<Outlet> outlets, Location location) {
        if (location == null || outlets == null)
            return;
        List<Outlet> outlets1 = new ArrayList<>();
        List<Outlet> outlets2 = new ArrayList<>();
        for (Outlet outlet : outlets) {

            if (outlet.getLocationId() == location.getId()) {
                outlets1.add(outlet);
            } else {
                if (!TextUtils.isEmpty(outlet.getSearchName())
                        && !TextUtils.isEmpty(location.getSearchName())
                        && outlet.getSearchName().equalsIgnoreCase(location.getSearchName()))
                    outlets1.add(outlet);
                else {
                    outlets2.add(outlet);
                }
            }
        }
        outlets.clear();
        outlets.addAll(outlets1);
        outlets.addAll(outlets2);

    }

    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String convertToCurrencyString(long value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value));
    }

    public Location getLocation(Context context) {
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
        String locationjson = localCacheHandler.getString(TkpdCache.Key.KEY_DEALS_LOCATION, null);
        if (locationjson != null) {
            Gson gson = new Gson();
            location = gson.fromJson(locationjson, Location.class);
        }

        if (location == null) {
            location = new Location();
            location.setName(LOCATION_NAME);
            location.setId(LOCATION_ID);
            updateLocation(context, location);
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
            Toast.makeText(context, context.getResources().getString(com.tokopedia.digital_deals.R.string.cannot_find_application), Toast.LENGTH_SHORT).show();
        }
    }

    public void showSnackBarDeals(String text, Context context, ViewGroup coordinatorLayout, boolean locationToast) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(com.tokopedia.digital_deals.R.layout.custom_location_change_snackbar, null);
        TextView tvmsg = snackView.findViewById(com.tokopedia.digital_deals.R.id.tv_msg);
        if (locationToast) {
            String str = context.getResources().getString(com.tokopedia.digital_deals.R.string.location_changed_to);
            str += text.toUpperCase();
            tvmsg.setText(getLocationText(str, context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_44)));
        } else {
            snackView.findViewById(com.tokopedia.digital_deals.R.id.main_content).setBackgroundColor(context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_R100));
            snackView.findViewById(com.tokopedia.design.R.id.divider).setBackgroundColor(context.getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500));
            tvmsg.setText(text);
        }

        TextView okbtn = snackView.findViewById(com.tokopedia.digital_deals.R.id.snack_ok);
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

    private SpannableString getLocationText(String text, int color) {
        int startIndexOfLink = text.indexOf(":");
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndexOfLink, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), startIndexOfLink, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void shareDeal(String deeplinkSlug, Context context, String name, String imageUrl, String desktopUrl) {
        String uri = DealsUrl.AppLink.DIGITAL_DEALS + "/" + deeplinkSlug;
        shareDeal(context, uri, name, imageUrl, desktopUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap setBlur(Bitmap smallBitmap, float radius, Context context) {
        if (radius > MIN_RADIUS && radius <= MAX_RADIUS) {
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
        } else {
            return smallBitmap;
        }
    }

    private void shareDeal(Context context, String uri, String name, String imageUrl, String desktopUrl) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType("")
                .setName(name)
                .setUri(uri)
                .setDesktopUrl(desktopUrl)
                .setImgUri(imageUrl)
                .build();

        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                DataMapper.getLinkerShareData(shareData), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, linkerShareData.getUrl());
                        share.putExtra(Intent.EXTRA_HTML_TEXT, linkerShareData.getUrl());
                        Intent intent = Intent.createChooser(share, context.getResources().getString(com.tokopedia.digital_deals.R.string.share_link));
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);

                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                }));

    }
}
