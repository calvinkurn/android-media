## Universal Media Editor

### Rules
Universal Media Editor is a new UI of editing tools for Image and Video with an immersive mode.
Which means the user will get an amazing experience to design their media for customizable.

### Applink
`tokopedia-android-internal://global/universal-editor`

### Using Picker Builder (:poc)
```kotlin
val intent = MediaPicker.intent(context) {
   withImmersiveEditor() // with default configuration; or
    
    withImmersiveEditor { // with a custom configuration
        setTitle("")
    }
}
```

### Development Scopes

### Basic Rules
1. Sort the method based on modifier
    - variable
      - inject
      - public
      - protected
      - ui-component
      - private
        - val/var
        - late-init
    - override method
    - protected method
    - private method
    The sorting excluded for a parent-single-method, such as:
      - setScreenName()
2. Create a representative variable and function name
    - onDataChanged
    - setClickListener
    - addOnTextListener
    - etc.
3. If a particular view need to expose the listener, we can do something like this:
    ```kotlin
    class SampleFoo {
        interface Listener {
            fun onFooClicked()
        }
    }
    ```
4. Write the test exponentially based on code changes. This test will helps to ensure the feature validation and code quality.
5. Please do Control+Option+O (remove unused import) and Option+Command+L (reformat indent) before commit the file.

### Create a New Fragment
1. Create the layout XML and put it in respective layouts directory. Currently, we have directories for parent, image and vod.
2. Create a new class and extend with BaseEditorFragment()
3. If the fragment need to retain the data state, don't forget to override the `onLoadSavedState()`
4. Register the fragment on `EditorFragmentProvider` with provide the object on `FragmentEditorModule`'s DI.

### Cases
1. If the fragment needs a UiModel, please put the object with the same hierarchy with the Fragment, and don't forget to ensure
the UiModel is survive-able with configuration changes by maintaining it with `onSaveInstanceState`.

2023 | Media Platform Team.
