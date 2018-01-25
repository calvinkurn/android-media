package com.tokopedia.tkpd.campaign.configuration;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
public class AudioRecorder {

  final MediaRecorder recorder = new MediaRecorder();
  final String path;

  /**
   * Creates a new audio recording at the given path (relative to root of SD card).
   */
  public AudioRecorder(String path) {
    this.path = sanitizePath(path);
  }

  private String sanitizePath(String path) {
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    if (!path.contains(".")) {
      path += ".3gp";
    }
    return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
  }
  RecordCompleteListener recordCompleteListener;
  /**
   * Starts a new recording.
   */
  public void start(RecordCompleteListener listener) throws IOException {
    recordCompleteListener = listener;
    String state = android.os.Environment.getExternalStorageState();
    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
        throw new IOException("SD Card is not mounted.  It is " + state + ".");
    }

    // make sure the directory we plan to store the recording in exists
    File directory = new File(path).getParentFile();
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Path to file could not be created.");
    }

    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    recorder.setMaxDuration(6000);
    recorder.setOutputFile(path);
    recorder.prepare();
    recorder.start();
    recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
      @Override
      public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
          recorder.stop();
          recordCompleteListener.onRecordComplete();

        }
      }
    });
  }

  /**
   * Stops a recording that has been previously started.
   */
  public void stop() throws IOException {
    recorder.stop();
    recorder.release();
  }
  public interface RecordCompleteListener {
    public void onRecordComplete();
  }
}