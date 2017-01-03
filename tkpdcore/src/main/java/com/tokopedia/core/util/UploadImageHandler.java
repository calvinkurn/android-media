package com.tokopedia.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.RestClient;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.var.TkpdUrl;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

public class UploadImageHandler {
	// TODO untuk upload gambar
	public interface UploadImageListener {
		void onUploadSuccess(Bitmap bitmap, JSONObject Result);

		void onUploadFailed();
	}

	public interface UploadImageInterface {
		void onUploadStart();

		void onCancel();
	}
	private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private String fileLoc;
	private String uploadUrl = "";
	private ArrayList<String> Key = new ArrayList<String>();
	private ArrayList<String> Value = new ArrayList<String>();
	private Activity context;
	private String WSUrl;
	private UploadImageListener Listener;
	private UploadImageInterface Interface;
	private Bitmap tempPicToUpload;
	private JSONObject json_res;
	private String FileName;
	private String BAName = "";
	private int Retry = 0;
	private String UServerID;
	private Fragment fragment;
	private int Type = 0;

	public UploadImageHandler(Activity context, String WSUrl, String FileName) {
		this.context = context;
		this.WSUrl = WSUrl;
		this.FileName = FileName;
	}
	
	public UploadImageHandler(Activity context, Fragment fragment, String WSUrl, String FileName) {
		this.context = context;
		this.fragment = fragment;
		this.WSUrl = WSUrl;
		this.FileName = FileName;
		Type = 1;
	}

	public void SetByteArrayName(String name) {
		BAName = name;
	}

	public void Commit(UploadImageInterface IUInterface) {
		Interface = IUInterface;
		startDialog();
	}

	private void startDialog() {
		CommonUtils.dumper("state: " + Environment.getExternalStorageState());
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		// myAlertDialog.setTitle(context.getString(R.string.title_upload_option));
		myAlertDialog.setMessage(context
				.getString(R.string.dialog_upload_option));

		myAlertDialog.setPositiveButton(
				context.getString(R.string.title_gallery),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// intent = new Intent(Intent.ACTION_GET_CONTENT);
						if (!Environment.getExternalStorageState().equals(
								Environment.MEDIA_REMOVED)) {
//							Intent intent = new Intent(
//									Intent.ACTION_PICK,
//									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//							intent.setType("image/*");
							// intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
							// // Untuk menghilangkan pilihan non local seperti
							// picasa / dropbox /dll
							
							if (Type == 0) {
								Intent imageGallery = new Intent(context, GalleryBrowser.class);
								context.startActivityForResult(imageGallery , ImageGallery.TOKOPEDIA_GALLERY);
//								context.startActivityForResult(Intent.createChooser(intent, "Select picture"), GALLERY_PICTURE);
							} else {
								Intent imageGallery = new Intent(fragment.getActivity().getApplicationContext(), GalleryBrowser.class);
								fragment.startActivityForResult(imageGallery , ImageGallery.TOKOPEDIA_GALLERY);
//								fragment.startActivityForResult(Intent.createChooser(intent, "Select picture"), GALLERY_PICTURE);
							}
						} else {
							WarningDialog();
						}
					}

				});

		myAlertDialog.setNegativeButton(
				context.getString(R.string.title_camera),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						if (!Environment.getExternalStorageState().equals(
								Environment.MEDIA_REMOVED)) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							Uri fileuri = getOutputMediaFileUri();
							intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
							if (Type == 0) {
								context.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
							} else {
								fragment.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
							}
						} else {
							WarningDialog();
						}
					}
				});

		myAlertDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Interface.onCancel();
			}
		});
		Dialog dialog = myAlertDialog.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
	}

	private Uri getOutputMediaFileUri() {
		return MethodChecker.getUri(context, getOutputMediaFile());
	}

	private File getOutputMediaFile() {
		String imageCode = uniqueCode();
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + File.separator
						+ "Tokopedia" + File.separator);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
		// Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + imageCode + ".jpg");
		fileLoc = Environment.getExternalStorageDirectory() + File.separator
				+ "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";
		return mediaFile;
	}

	private String uniqueCode() {
		String IDunique = UUID.randomUUID().toString();
		String id = IDunique.replaceAll("-", "");
		String code = id.substring(0, 16);
		return code;
	}

	private void WarningDialog() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle(context.getString(R.string.title_warning));
		myAlertDialog.setMessage(context
				.getString(R.string.dialog_no_memory_card));

		myAlertDialog.setPositiveButton(context.getString(R.string.title_ok),
				null);

		myAlertDialog.show();
	}

	public void GenerateHost() {
		NetworkHandler network = new NetworkHandler(context,
				TkpdUrl.GENERATE_HOST);
		System.out.println("Generating Host");
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {

			}

			@Override
			public void getResponse(JSONObject Result) {
				System.out.println(Result.toString());
				try {
					uploadUrl = "http://" + Result.getString("upload_host")
							+ "/ajax/ws/" + WSUrl;
					UServerID = Result.getString("server_id");
					UploadImage uploadTask = new UploadImage();
					uploadTask.execute();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {

			}
		});
	}

	public void setListener(UploadImageListener _Listener){
		Listener = _Listener;
	}

	public void getOnResult(int requestCode, int resultCode, Intent data,
			UploadImageListener _Listener) {
		Listener = _Listener;
		if (requestCode == ImageGallery.TOKOPEDIA_GALLERY) {
			if (resultCode == ImageGallery.RESULT_CODE && data != null) {
				try {
					Interface.onUploadStart();
					Log.i("request code", Integer.toString(requestCode));

					BitmapFactory.Options options = new BitmapFactory.Options();
					BitmapFactory.Options checksize = new BitmapFactory.Options();
					checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
					checksize.inJustDecodeBounds = true;
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//					Uri fileuri = data.getData();
					Bitmap tempPic;
					Resources r = context.getResources();
					float px = TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 300,
							r.getDisplayMetrics());
//					if (fileuri.toString().contains(
//							"com.google.android.gallery3d.provider")) {
//						System.out.println("Magic URI is: "
//								+ fileuri.toString());
//						// tempPic =
//						// BitmapFactory.decodeStream(res.openInputStream(fileuri),
//						// null, options);
//						getBitmap("sementara", fileuri, options);
//					} else {
//						fileLoc = getRealPathFromURI(context, fileuri);
						fileLoc = data.getStringExtra(ImageGallery.EXTRA_URL);
						BitmapFactory.decodeFile(fileLoc, checksize);
						options.inSampleSize = ImageHandler
								.calculateInSampleSize(checksize);
						tempPic = BitmapFactory.decodeFile(fileLoc, options);
						Bitmap thumbBitmap = ImageHandler.ResizeBitmap(tempPic,
								px);
						try {
							thumbBitmap = ImageHandler.RotatedBitmap(
									thumbBitmap, fileLoc);
						} catch (IOException e) {
							Log.e("CAMERA", e.getMessage());
						}
						tempPic.recycle();
						tempPic = null;

						if (thumbBitmap != null) {
							GenerateHost();
							// UploadImage uploadTask = new UploadImage();
							// uploadTask.execute();
						}
//					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(
							context,
//							"Gambar ini tidak valid karena tidak melalui gallery. Mohon memilih gambar melalui gallery Anda",
							"Gambar ini tidak valid",
							Toast.LENGTH_SHORT).show();
					if(Listener != null)
						Listener.onUploadFailed();
					e.printStackTrace();
				}
			}
		} else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == Activity.RESULT_OK) {
				Interface.onUploadStart();
				BitmapFactory.Options options = new BitmapFactory.Options();
				BitmapFactory.Options checksize = new BitmapFactory.Options();
				checksize.inJustDecodeBounds = true;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Resources r = context.getResources();
				float px = TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
								r.getDisplayMetrics());
				BitmapFactory.decodeFile(fileLoc, checksize);
				options.inSampleSize = ImageHandler
						.calculateInSampleSize(checksize);
				Bitmap tempPic = BitmapFactory.decodeFile(fileLoc, options);
				Bitmap thumbBitmap = ImageHandler.ResizeBitmap(tempPic, px);
				try {
					thumbBitmap = ImageHandler.RotatedBitmap(thumbBitmap,
							fileLoc);
				} catch (IOException e) {
					Log.e("CAMERA", e.getMessage());
				}
				tempPic.recycle();
				tempPic = null;
				if (thumbBitmap != null) {
					GenerateHost();
					// UploadImage uploadTask = new UploadImage();
					// uploadTask.execute();
				}

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}

		}
	}
	
	private void getBitmap(String string, Uri fileuri, BitmapFactory.Options option) {
		    try {
				final InputStream is = context.getContentResolver().openInputStream(fileuri);
				tempPicToUpload = BitmapFactory.decodeStream(is, null, option);
				is.close();
				System.out.println("Magic Is Close");
				GenerateHost();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public class UploadImage extends AsyncTask<Void, Void, String> {
		public UploadImage() {
		}

		@Override
		protected String doInBackground(Void... Void) {
			try {
//				Thread.sleep(1000);
				return UploadToServer();
			} catch (Exception e) {
				return null;
			}

		}

		@Override
		protected void onPostExecute(final String success) {
			if(success != null)
				if(Listener != null)
					Listener.onUploadSuccess(tempPicToUpload, json_res);
			else {
				CommonUtils.UniversalToast(context, context.getResources().getString(R.string.msg_upload_error));
				if(Listener != null)
					Listener.onUploadFailed();
			}
		}

	}

	public void AddEntity(String Key, String Value) {
		this.Key.add(Key);
		this.Value.add(Value);
	}

	private String UploadToServer() throws Exception{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		if(tempPicToUpload == null){
			System.out.println("Magic ALL GOES WELL");
			System.out.println("WSURl: " + uploadUrl);
			System.out.println("fileloc: " + fileLoc);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// Untuk menghitung ukuran image sebelum diload ke memory
			BitmapFactory.Options checksize = new BitmapFactory.Options();
			checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
			checksize.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileLoc, checksize);
			options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
			Bitmap tempPic = BitmapFactory.decodeFile(fileLoc, options);
			try {
				tempPic = ImageHandler.RotatedBitmap(tempPic, fileLoc);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
				tempPicToUpload = ImageHandler.ResizeBitmap(tempPic, 2048);
			} else if (tempPic.getWidth() < 300 || tempPic.getHeight() < 300) {
				tempPicToUpload = ImageHandler.ResizeBitmap(tempPic, 300);
			} else {
				tempPicToUpload = tempPic;
			}
		}
		else{
			if (tempPicToUpload.getWidth() > 2048 || tempPicToUpload.getHeight() > 2048) {
				tempPicToUpload = ImageHandler.ResizeBitmap(tempPicToUpload, 2048);
			} else if (tempPicToUpload.getWidth() < 300 || tempPicToUpload.getHeight() < 300) {
				tempPicToUpload = ImageHandler.ResizeBitmap(tempPicToUpload, 300);
			}
		}
		tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
		byte[] ba = bao.toByteArray();
		// String ba1 = base.encodeBytes(ba);
		ByteArrayBody bab;
		if (BAName.length() == 0)
			bab = new ByteArrayBody(ba, "image.jpg");
		else
			bab = new ByteArrayBody(ba, BAName);
		RestClient rclient = new RestClient(uploadUrl, 1);
		try {
			rclient.addEntityPart("user_id", SessionHandler.getLoginID(context));
			rclient.addEntityPart("server_id", UServerID);
			System.out.println("ServerID Magic: " + UServerID);
			rclient.addEntityPart(
					"User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			System.out.println(SessionHandler.getLoginID(context));
			for (int i = 0; i < Key.size(); i++) {
				rclient.addEntityPart(Key.get(i), Value.get(i));
			}
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Catch");
			e1.printStackTrace();
		}
		// rclient.AddHeader("Content-type", "multipart/form-data");
		rclient.addEntityByte(FileName, bab);
		try {
			rclient.ExecuteMultipart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = rclient.getResponse();
		JSONObject json_o = new JSONObject(response);
		json_res = new JSONObject(json_o.getString("result"));
		json_res.put("server_id", UServerID);
		return response;
	}

}
