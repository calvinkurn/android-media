import React, { Component } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';


const green = '#37b24d';
const orange = '#fd7e14'; 
const white = '#FFFFFF';

class Page_Dua extends Component{
	render(){
		const {name} = this.props.navigation.state.params;

		return(
			<View style={{flex:1, alignItems:'center'}}>
				<Text style={{margin:15}}>Welcome {name}, {'\n'}ini page dua!</Text> 
			</View>
		)
	}
}

export default Page_Dua;