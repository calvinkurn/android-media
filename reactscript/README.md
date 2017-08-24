## React Native Configuration inside Android Native App
1. Make sure you already install NodeJS
2. Do `npm install` inside android-tokopedia-core root project

## Running Project
1. Run app via Android Studio
2. Inside android-tokopedia-core root project, do `npm start` to run the server

## Add New Feature
1. Place your new React Native project inside `reactscript/src/pages/` folder
2. Don't forget to initialize your app into the router inside `reactscript/src/configs/router.js`
3. Call your React Native app using conditional render in `reactscript/index.android.js` file 
4. If necessary, add dependencies in `package.json` inside root_project folder 
5. If necessary, add common components in `reactscript/src/components/common` inside root_project folder (Don't forget ro register the component in `index.js` file)






