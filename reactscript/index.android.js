'use strict';

import React, { Component } from 'react';
import codePush from "react-native-code-push";
import {
  AppRegistry,
  StyleSheet,
  Text,
  AppState,
  AsyncStorage,
  View
} from 'react-native';
import { NavigationModule, NetworkModule } from 'NativeModules';
import { HotList_ } from './src/configs/router';
import OfficialStore from './src/pages/official-store/setup'


// let codePushOptions = { checkFrequency: codePush.CheckFrequency.ON_APP_RESUME };

class Home extends Component {
  componentWillMount() {
    if (this.props.User_ID != ''){
      AsyncStorage.setItem('user_id', this.props.User_ID);
    } else {
      AsyncStorage.removeItem('user_id').then(res => { })
      AsyncStorage.getItem('user_id').then(uid => { })
    }
  }
  

  render(){
    if (this.props.Screen == 'HotList'){
      return <HotList_ />
    } else if (this.props.Screen == 'official-store'){
      return <OfficialStore screenProps={{ User_ID: this.props.User_ID, Screen: this.props.Screen }}  /> 
    } else {
      return(
        <View style={{justifyContent:'center', alignItems:'center', flex:1}}>
          <ActivityIndicator size="large" />
        </View>
      )
    }
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

// Home = codePush({ checkFrequency: codePush.CheckFrequency.ON_APP_RESUME, installMode: codePush.InstallMode.ON_NEXT_RESUME })(Home);
module.exports = Home;
AppRegistry.registerComponent('MAIN', () => Home);

