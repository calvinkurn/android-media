package com.tokopedia.tkpd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.customadapter.TouchImageAdapter;
import com.tokopedia.tkpd.customadapter.TouchImageAdapter.OnImageStateChange;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PreviewProductImage extends TActivity {

    private TouchViewPager vp;
    private TextView Desc;
    private TouchImageAdapter adapter;
    private ArrayList<String> fileLoc;
    private ArrayList<String> imgDesc;
    private int pos;
    private int lastPos = 0;

    private SimpleTarget<Bitmap> target2 = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            CommonUtils.SaveImageFromBitmap(PreviewProductImage.this, resource, processPicName(vp.getCurrentItem()));
            Toast.makeText(PreviewProductImage.this, R.string.download_complete, Toast.LENGTH_SHORT).show();
        }
    };

    private String processPicName(int index) {
        String picName = new String();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String dateString = sdf.format(date);
        try {
            picName = getIntent().getExtras().getString("product_name", dateString).replaceAll("[^a-zA-Z0-9]", "") + "_" + Integer.toString(index + 1);
        } catch (NullPointerException e) {
            finish();
        }
        return picName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        hideToolbar();
        inflateView(R.layout.activity_preview_product_image);

        fileLoc = getIntent().getExtras().getStringArrayList("fileloc");
        imgDesc = getIntent().getExtras().getStringArrayList("image_desc");
        pos = getIntent().getExtras().getInt("img_pos");
        vp = (TouchViewPager) findViewById(R.id.view_pager);
        Desc = (TextView) findViewById(R.id.desc);
        Log.i("file_loc", fileLoc.toString());
        if (imgDesc == null) {
            Desc.setVisibility(View.GONE);
        } else {
            Desc.setText(Html.fromHtml(imgDesc.get(0)));
        }

        findViewById(R.id.download_image).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // CommonUtils.SaveImages(PreviewProductImage.this,
                        // fileLoc.get(vp.getCurrentItem()));

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                PreviewProductImage.this,
                                AlertDialog.THEME_HOLO_LIGHT);
                        // builder.onSuccessGetInboxTicketDetail(getLayoutInflater().inflate(R.layout.your_layout,
                        // null));
                        builder.setMessage(getString(R.string.dialog_save_preview_product_image));
                        builder.setPositiveButton(
                                getString(R.string.title_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ImageHandler.loadImageBitmap2(getApplicationContext(), fileLoc.get(vp.getCurrentItem()), target2);
//										PicassoHelper.getPicasso().load(fileLoc.get(vp.getCurrentItem())).into(target);
                                    }
                                });
                        builder.setNegativeButton(getString(R.string.title_no),
                                null);
                        // Dialog dialog = new Dialog(context);
                        Dialog dialog = builder.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                });
        adapter = new TouchImageAdapter(PreviewProductImage.this, fileLoc, 100);

        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 != lastPos) {
                    if (imgDesc == null) {
                        Desc.setVisibility(View.GONE);
                    } else {
                        Desc.setText(imgDesc.get(arg0));
                    }
                    lastPos = arg0;
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        adapter.SetonImageStateChangeListener(new OnImageStateChange() {

            @Override
            public void OnStateZoom() {
                vp.SetAllowPageSwitching(false);
            }

            @Override
            public void OnStateDefault() {
                vp.SetAllowPageSwitching(true);
            }
        });
        vp.setAdapter(adapter);
        vp.setCurrentItem(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_save_button_img, menu);
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
