import React, { Component } from 'react';
import { View, Text, TouchableOpacity } from 'react-native'; 


const violet = '#7950f2';
const blue = '#228ae6';
const green = '#37b24d';
const orange = '#fd7e14'; 
const white = '#FFFFFF';

class ContactUs extends Component{
	render(){
		return(
			<View style={{flex:1, alignItems:'center'}}>
				<Text style={{margin:15}}>ini page favorite!</Text>
				<TouchableOpacity 
					onPress={() => this.props.navigation.navigate('PageDua', {
						name: 'Yogie'
					})}
					style={{backgroundColor:green, width:'40%', borderWidth:1, borderRadius:5, borderColor:green}}>
					<Text style={{margin:15, color:white, alignSelf:'center'}}>Go to Page 2</Text>
				</TouchableOpacity>
			</View>
		)
	}
}

export default ContactUs;