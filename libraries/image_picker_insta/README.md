# How to use
working branch - origin/feature/rahullohra2903/image_picker_insta
Library to include -
implementation project(":libraries:image_picker_insta")
OR
implementation project(rootProject.ext.libraries.imagePickerInsta)


- Camera Crop
  - https://developer.android.com/reference/androidx/camera/core/ViewPort
  - https://stackoverflow.com/questions/62375930/how-can-i-crop-the-live-feed-from-the-camera-on-each-frame-on-android
  
- Video Codec
  - https://www.youtube.com/watch?v=CrskWnKG8pk
  
- Pending
  - Progress bar with round corner is
  - Check whether we can turn on selfie camera in Asus Zenfone 6 
  - After clicking new picture it must reflect in image picker section
  - Logic of recent folder - there is no such recent folder
  - Show video thumbnails and timer on video thumbnail
  - Use thumbnail content uri for faster loading
  - Check for rotation and tablet behaviour
  
- Helpful class
  - TkpdVideoPlayer
  
  
- PE
  - Need PIC for android storage
  - Need to know what type of media we can store, where we can store, naming conventions


MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
retriever.setDataSource(context, Uri.fromFile(videoFile));
String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
long timeInMillisec = Long.parseLong(time );

retriever.release()