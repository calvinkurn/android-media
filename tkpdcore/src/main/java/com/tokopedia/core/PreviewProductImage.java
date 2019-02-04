package com.tokopedia.core;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.TouchImageAdapter;
import com.tokopedia.core.customadapter.TouchImageAdapter.OnImageStateChange;
import com.tokopedia.core.gcm.utils.NotificationChannelId;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import com.tokopedia.core2.R2;
import com.tokopedia.core2.R;

@RuntimePermissions
public class PreviewProductImage extends TActivity {

    @BindView(R2.id.view_pager)
    public TouchViewPager vpImage;
    @BindView(R2.id.desc)
    public TextView tvDescription;
    @BindView(R2.id.download_image)
    public TextView tvDownload;
    private TouchImageAdapter adapter;
    private ArrayList<String> fileLocations;
    private ArrayList<String> imageDescriptions;
    private int lastPos = 0;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        hideActionBar();
        inflateView(R.layout.activity_preview_product_image);
        ButterKnife.bind(this);
        Bundle extras;

        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            fileLocations = extras.getStringArrayList("fileloc");
            imageDescriptions = extras.getStringArrayList("image_desc");
            position = extras.getInt("img_pos");
        } else {
            fileLocations = new ArrayList<>();
            imageDescriptions = new ArrayList<>();
        }

        adapter = new TouchImageAdapter(PreviewProductImage.this, fileLocations, );
        setViewListener();
    }


    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void setViewListener() {
        if (imageDescriptions == null) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setText(MethodChecker.fromHtml(imageDescriptions.get(0)));
        }
        tvDownload.setOnClickListener(getDownloadClickListener());

        vpImage.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 != lastPos) {
                    if (imageDescriptions == null) {
                        tvDescription.setVisibility(View.GONE);
                    } else {
                        tvDescription.setText(imageDescriptions.get(arg0));
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
                vpImage.SetAllowPageSwitching(false);
            }

            @Override
            public void OnStateDefault() {
                vpImage.SetAllowPageSwitching(true);
            }
        });
        vpImage.setAdapter(adapter);
        vpImage.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save_button_img, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = MethodChecker.getUri(getApplicationContext(), file);
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        );
        startActivity(intent);
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

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_IMAGE_PREVIEW;
    }

    @SuppressLint("SimpleDateFormat")
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

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void actionDownloadAndSavePicture() {
        final String filenameParam = processPicName(vpImage.getCurrentItem()) + ".jpg";
        Random random = new Random();
        final int randomNotificationId = generateRandomInteger(random);
        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(PreviewProductImage.this, NotificationChannelId.GENERAL);
        notificationBuilder.setContentTitle(filenameParam)
                .setContentText(getString(R.string.download_in_process))
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_notify))
                .setAutoCancel(true)
        ;
        notificationBuilder.setProgress(0, 0, true);
        notificationManager.notify(randomNotificationId, notificationBuilder.build());

        SimpleTarget<Bitmap> targetListener = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                final String path = CommonUtils.SaveImageFromBitmap(PreviewProductImage.this,
                        resource, filenameParam);
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
                    Uri uri = MethodChecker.getUri(getApplicationContext(), file);
                    intent.setDataAndType(uri, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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
        ImageHandler.loadImageBitmap2(getApplicationContext(), fileLocations.get(vpImage.getCurrentItem()), targetListener);
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
