package com.tokopedia.core;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.TouchImageAdapter;
import com.tokopedia.core.customadapter.TouchImageAdapter.OnImageStateChange;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PreviewProductImage extends TActivity {

    private TouchViewPager vp;
    private TextView Desc;
    private TouchImageAdapter adapter;
    private ArrayList<String> fileLoc;
    private ArrayList<String> imgDesc;
    private int lastPos = 0;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_IMAGE_PREVIEW;
    }


    private String processPicName(int index) {
        String picName = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String dateString = sdf.format(date);
        try {
            picName = getIntent().getExtras().getString("product_name", dateString)
                    .replaceAll("[^a-zA-Z0-9]", "") + "_" + Integer.toString(index + 1);
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
        int pos = getIntent().getExtras().getInt("img_pos");
        vp = (TouchViewPager) findViewById(R.id.view_pager);
        Desc = (TextView) findViewById(R.id.desc);
        if (imgDesc == null) {
            Desc.setVisibility(View.GONE);
        } else {
            Desc.setText(Html.fromHtml(imgDesc.get(0)));
        }
        TextView tvDownload = (TextView) findViewById(R.id.download_image);
        tvDownload.setOnClickListener(getDownloadClickListener());
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

    private int generateRandomInteger(Random aRandom) {
        long range = (long) 1001 - (long) 100 + 1;
        long fraction = (long) (range * aRandom.nextDouble());
        return (int) (fraction + 1);
    }

    private void openImageDownloaded(String path) {
        File file = new File(path);
        Intent sintent = new Intent();
        sintent.setAction(Intent.ACTION_VIEW);
        sintent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(sintent);
        PreviewProductImage.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public OnClickListener getDownloadClickListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDownloadDialog();
            }
        };
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                PreviewProductImage.this,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(getString(R.string.dialog_save_preview_product_image));
        builder.setPositiveButton(
                getString(R.string.title_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        PreviewProductImagePermissionsDispatcher.actionDownloadAndSavePictureWithCheck(PreviewProductImage.this);
                    }
                });
        builder.setNegativeButton(getString(R.string.title_no),
                null);
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.show();
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void actionDownloadAndSavePicture() {
        String filename = processPicName(vp.getCurrentItem()) + ".jpg";
        Random random = new Random();
        final int randomNotificationId = generateRandomInteger(random);
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(PreviewProductImage.this);
        notificationBuilder.setContentTitle(filename)
                .setContentText(getString(R.string.download_in_process))
                .setSmallIcon(R.drawable.qc_launcher)
                .setAutoCancel(true)
        ;
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(randomNotificationId, notificationBuilder.build());

        SimpleTarget<Bitmap> target2 = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                final String path = CommonUtils.SaveImageFromBitmap(PreviewProductImage.this,
                        resource, processPicName(vp.getCurrentItem()));
                if (path == null) {
                    notificationBuilder.setContentText(getString(R.string.download_failed))
                            .setProgress(0, 0, false);
                    notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(randomNotificationId, notificationBuilder.build());
                    SnackbarManager.make(PreviewProductImage.this,
                            getString(R.string.download_failed),
                            Snackbar.LENGTH_SHORT)
                            .setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);
                                    PreviewProductImage.this.getWindow()
                                            .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                }
                            })
                            .show();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File file = new File(path);
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                    PendingIntent pIntent = PendingIntent.getActivity(PreviewProductImage.this, 0, intent, 0);

                    notificationBuilder.setContentText(getString(R.string.download_success))
                            .setProgress(0, 0, false)
                            .setContentIntent(pIntent);

                    notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(randomNotificationId, notificationBuilder.build());

                    PreviewProductImage.this.getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    SnackbarManager.make(PreviewProductImage.this,
                            getString(R.string.download_success),
                            Snackbar.LENGTH_SHORT).setAction(R.string.preview_picture_open_action,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openImageDownloaded(path);
                                }
                            }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            PreviewProductImage.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                    }).show();
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                notificationBuilder.setContentText(getString(R.string.download_failed))
                        .setProgress(0, 0, false);
                notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(randomNotificationId, notificationBuilder.build());
                PreviewProductImage.this.getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                SnackbarManager.make(PreviewProductImage.this,
                        getString(R.string.download_failed),
                        Snackbar.LENGTH_SHORT)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                PreviewProductImage.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            }
                        })
                        .show();
            }
        };
        ImageHandler.loadImageBitmap2(getApplicationContext(), fileLoc.get(vp.getCurrentItem()), target2);
    }

    @OnShowRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForWriteExternal(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForWriteExternal() {
        RequestPermissionUtil.onPermissionDenied(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        finish();
    }

    @OnNeverAskAgain(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForWriteExternal() {
        RequestPermissionUtil.onNeverAskAgain(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PreviewProductImagePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
